provider "helm" {
  kubernetes {
    config_path = "/root/.kube/config"
  }
}

provider "kubernetes" {
  config_path = "/root/.kube/config"
}


resource "kubernetes_namespace" "test-namespace" {
  metadata {
        name = "test"
  }
}

resource "helm_release" "otel" {
  name       = "otel"
  namespace  = "test"
  

  #repository = "https://charts.bitnami.com/bitnami"
  chart      = "/root/monitoring-kube-metrics-prometheus-grafana-terraform/otel"

  set {
    name  = "service.type"
    value = "LoadBalancer"
  }
}

resource "helm_release" "quarkas-demo" {
  name       = "quarkas-demo"
  namespace  = "test"
  

  #repository = "https://charts.bitnami.com/bitnami"
  chart      = "/root/monitoring-kube-metrics-prometheus-grafana-terraform/quarkas-demo"

  set {
    name  = "service.type"
    value = "LoadBalancer"
  }
}



