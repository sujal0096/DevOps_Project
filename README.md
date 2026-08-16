# Employee Management — DevOps CI/CD Project

A complete DevOps project demonstrating the CI/CD lifecycle of a Spring Boot REST API using Jenkins, Maven, Nexus, SonarQube, Docker, Trivy, Docker Hub, Kubernetes (K3s), Helm, Prometheus and Grafana.

---

## Project Overview

This project contains an Employee Management REST API developed using Spring Boot.

The application provides CRUD operations for employee data and uses PostgreSQL as the database.

The project demonstrates how source code moves from development to production through an automated CI/CD pipeline.

---

## Application Architecture

```text
                    Developer
                        |
                        v
                     GitHub
                        |
                        v
                    Jenkins
                        |
            +-----------+-----------+
            |                       |
            v                       v
       Maven Build             SonarQube
            |                       |
            v                       v
         Nexus                 Quality Gate
            |
            v
      Docker Image Build
            |
            v
        Trivy Scan
            |
            v
        Docker Hub
            |
            v
          K3s
            |
       +----+----+
       |         |
       v         v
 Spring Boot  PostgreSQL
    Pods
       |
       v
    Service
       |
       v
     Users


Monitoring:

        K3s Cluster
             |
             v
        Prometheus
             |
       Collects Metrics
             |
             v
          Grafana
             |
             v
       Dashboards
