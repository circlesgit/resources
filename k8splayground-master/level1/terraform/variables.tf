variable "project" {
  default = ""
  description = "GCP project name"
}
variable "region" {
  default = ""
  description = "GCP Region"
}
variable "zone" {
  default = "us-central1-c"
  description = "GCP Zone"
}
variable "gke_name" {
  default = ""
  description = "GKE cluster name"
}
variable "gke_nodepool_name" {
  default = ""
  description ="GKE Nodepool name"
}
variable "gke_nodepool_size" {
  default = 3
  description = "GKE Nodepool size"
}
variable "vpcname" {
  default = ""
  description = "GCP VPC Name"
}

variable "subnet" {
  default = ""
  description = "GCP VPC Subnet Name"
}
variable "ip_cidr_range" {
  default = "10.10.1.0/24"
  description = "ip_cidr_range"
}
variable notify {
  type = "list"
  description = "list of channels to notify"
}
variable escalation_notify {
  type = "list"
  description = "list of channels to escalation"
}
variable runbook_url {
  default = "runbook_url"
  description = "runbook url for the alert"
}
