FROM node:23.4-alpine AS build

WORKDIR /app
USER root

COPY . .

RUN npm install
RUN npm run build

FROM nginx:1.25-alpine

USER root

WORKDIR /usr/share/nginx/html

COPY --from=build /app/dist/. ./
COPY ./docker/nginx/nginx.conf /etc/nginx/nginx.conf
