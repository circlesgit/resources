package main

import (
	// plugin "github.com/hashicorp/go-plugin"
	//tplugin "github.com/hashicorp/terraform/plugin"
	"fmt"
  terraform "github.com/hashicorp/terraform/terraform"
	tform "github.com/hashicorp/terraform/builtin/providers/terraform"
  "terraform-providers/terraform-provider-google"
)

func main() {
	a := tform.NewProvider()
	fmt.Println(a)
	var b terraform-provider-google
}
