data "terraform_remote_state" "aks" {
  backend = "local"
  config = {
    path = "../../azure-terraform/terraform-aks-setup/terraform.tfstate"
  }
}

data "azurerm_kubernetes_cluster" "aks" {
  name                = var.cluster_name
  resource_group_name = var.resource_group_name
}

provider "kubernetes" {
  host                   = data.azurerm_kubernetes_cluster.aks.kube_config.0.host
  username               = data.azurerm_kubernetes_cluster.aks.kube_config.0.username
  password               = data.azurerm_kubernetes_cluster.aks.kube_config.0.password
  client_certificate     = base64decode(data.azurerm_kubernetes_cluster.aks.kube_config.0.client_certificate)
  client_key             = base64decode(data.azurerm_kubernetes_cluster.aks.kube_config.0.client_key)
  cluster_ca_certificate = base64decode(data.azurerm_kubernetes_cluster.aks.kube_config.0.cluster_ca_certificate)
}

provider "helm" {
  kubernetes {
  host                   = data.azurerm_kubernetes_cluster.aks.kube_config.0.host
  username               = data.azurerm_kubernetes_cluster.aks.kube_config.0.username
  password               = data.azurerm_kubernetes_cluster.aks.kube_config.0.password
  client_certificate     = base64decode(data.azurerm_kubernetes_cluster.aks.kube_config.0.client_certificate)
  client_key             = base64decode(data.azurerm_kubernetes_cluster.aks.kube_config.0.client_key)
  cluster_ca_certificate = base64decode(data.azurerm_kubernetes_cluster.aks.kube_config.0.cluster_ca_certificate)
}
}

resource helm_release otel-release {
  name       = "otel-release-controller"

# repository = "https://github.com/prathibhapadma/quarkus-otel-test-repo/tree/master/charts"
  chart      = "../../helm-charts/opentelementry"

  set {
    name  = "service.type"
    value = "LoadBalancer"
  }
}

resource helm_release quarkus-app-release {
  name       = "quarkus-app-controller"

 #repository = "https://github.com/prathibhapadma/quarkus-otel-test-repo/tree/master/charts"
  chart      = "../../helm-charts/demo-quarkus"

  set {
    name  = "service.type"
    value = "LoadBalancer"
  }
}
