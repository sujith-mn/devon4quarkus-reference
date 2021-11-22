
resource "random_pet" "prefix" {}

provider "azurerm" {
  features {}
}

resource "azurerm_resource_group" "default" {
  name     = "${random_pet.prefix.id}-rg"
  location = "West US 2"

  tags = {
    environment = "Demo"
  }
}

resource "azurerm_kubernetes_cluster" "default" {
  name                = "${random_pet.prefix.id}-aks"
  location            = azurerm_resource_group.default.location
  resource_group_name = azurerm_resource_group.default.name
  dns_prefix          = "${random_pet.prefix.id}-k8s"

  default_node_pool {
    name            = "default"
    node_count      = 2
    vm_size         = "Standard_D2_v2"
    os_disk_size_gb = 30
  }

  service_principal {
    client_id     = var.appId
    client_secret = var.password
  }

  role_based_access_control {
    enabled = true
  }

  tags = {
    environment = "Demo"
 }
}

provider helm {
  kubernetes {
    #config_path = "~/.kube/config"
    config_path = "/root/.kube/config"
  }
}

resource helm_release otel-release {
  name       = "otel-release-controller"

  #repository = " https://github.com/prathibhapadma/helm-charts/charts/helm"
  chart      = "/root/devon4quarkus-reference/charts/helm"

  set {
    name  = "service.type"
    value = "LoadBalancer"
  }
}

resource helm_release quarkus-app-release {
  name       = "quarkus-app-controller"

  repository = "https://github.com/prathibhapadma/helm-charts/charts/quarkus-app"
  chart      = "/root/devon4quarkus-reference/charts/quarkus-app"

  set {
    name  = "service.type"
    value = "LoadBalancer"
  }
}

