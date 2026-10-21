terraform {
  required_providers { aws = { source = "hashicorp/aws", version = "~> 5.0" } }
  required_version = ">= 1.6"
}
provider "aws" { region = var.aws_region }

resource "aws_ecr_repository" "api" {
  name         = "credit-radar-api"
  force_delete = true
}
resource "aws_db_instance" "postgres" {
  identifier              = "credit-radar-postgres"
  engine                  = "postgres"
  engine_version          = "16"
  instance_class          = "db.t4g.micro"
  allocated_storage       = 20
  db_name                 = "creditradar"
  username                = var.db_username
  password                = var.db_password
  publicly_accessible     = false
  skip_final_snapshot     = true
  backup_retention_period = 7
}

# Esta base declara os serviços essenciais. Para produção, adicione VPC privada,
# security groups, ALB e Secrets Manager antes de criar o serviço ECS.
output "ecr_repository_url" { value = aws_ecr_repository.api.repository_url }
output "rds_endpoint" { value = aws_db_instance.postgres.address }
