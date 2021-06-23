#!/usr/bin/env bash

mkdir microservices
cd microservices

spring init \
--boot-version=2.2.2.RELEASE \
--build=gradle \
--java-version=1.8 \
--packaging=jar \
--name=product-service \
--package-name=io.github.ddojai.microservices.core.product \
--groupId=io.github.ddojai.microservices.core.product \
--dependencies=actuator,webflux \
--version=1.0.0-SNAPSHOT \
product-service

spring init \
--boot-version=2.2.2.RELEASE \
--build=gradle \
--java-version=1.8 \
--packaging=jar \
--name=review-service \
--package-name=io.github.ddojai.microservices.core.review \
--groupId=io.github.ddojai.microservices.core.review \
--dependencies=actuator,webflux \
--version=1.0.0-SNAPSHOT \
review-service

spring init \
--boot-version=2.2.2.RELEASE \
--build=gradle \
--java-version=1.8 \
--packaging=jar \
--name=recommendation-service \
--package-name=io.github.ddojai.microservices.core.recommendation \
--groupId=io.github.ddojai.microservices.core.recommendation \
--dependencies=actuator,webflux \
--version=1.0.0-SNAPSHOT \
recommendation-service

spring init \
--boot-version=2.2.2.RELEASE \
--build=gradle \
--java-version=1.8 \
--packaging=jar \
--name=product-composite-service \
--package-name=io.github.ddojai.microservices.composite.product \
--groupId=io.github.ddojai.microservices.composite.product \
--dependencies=actuator,webflux \
--version=1.0.0-SNAPSHOT \
product-composite-service

cd ..