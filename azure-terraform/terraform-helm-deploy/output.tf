output "Helm-charts" {
  value = helm_release.otel-release.name
}

output "Helm-Charts" {
  value = helm_release.quarkus-app-release.name
}
