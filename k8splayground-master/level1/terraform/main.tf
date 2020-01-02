provider "google" {
  project = "${var.project}"
  region  = "${var.region}"
  zone    = "${var.zone}"
}

resource "google_compute_network" "vpc_network" {
  name                    = "${var.vpcname}-vpc1"
  auto_create_subnetworks = false
  project                 = "${var.project}"
}

data "google_compute_subnetwork" "my-subnetwork" {
  name          = "${var.subnet}-subnet"
  region        = "${var.region}"
  project       = "${var.project}"
  ip_cidr_range = "${var.ip_cidr_range}"
}

resource "google_container_cluster" "primary" {
  name                     = "${var.gke_name}"
  location                 = "${var.region}"
  network                  = "${google_compute_network.vpc_network.name}"
  subnetwork               = "${google_compute_subnetwork.my-subnetwork.name}"
  remove_default_node_pool = true
  initial_node_count       = 1

  master_auth {
    username = ""
    password = ""

    client_certificate_config {
      issue_client_certificate = false
    }
  }
}

resource "google_container_node_pool" "primary_nodepool_nodes" {
  name       = "${var.gke_nodepool_name}"
  location   = "${var.region}"
  cluster    = "${google_container_cluster.primary.name}"
  node_count = "${var.gke_nodepool_size}"

  node_config {
    preemptible  = false
    machine_type = "n1-standard-1"
    disk_size_gb    = "100"
    metadata = {
      disable-legacy-endpoints = "true"
    }

    oauth_scopes = [
      "https://www.googleapis.com/auth/compute",
      "https://www.googleapis.com/auth/devstorage.read_only",
      "https://www.googleapis.com/auth/logging.write",
      "https://www.googleapis.com/auth/monitoring",
    ]
  }
}

output "k8s_cluster_name" {
  value = "${google_container_cluster.primary.name}"
}
