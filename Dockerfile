# Etapa 1: build Angular
FROM node:20-alpine AS builder

WORKDIR /app
COPY . .
RUN npm config set registry https://registry.npmjs.org/
RUN npm config set strict-ssl false
RUN npm install
RUN npm run build

# Etapa 2: servir con nginx
FROM nginx:alpine

#  ^|^e ELIMINAR ARCHIVOS POR DEFECTO DE NGINX
RUN rm -rf /usr/share/nginx/html/*

#  ^|^e COPIAR TU APLICACI ^sN ANGULAR (dist/.../browser)
COPY --from=builder /app/dist/copichat/browser/ /usr/share/nginx/html/

#  ^|^e CONFIGURACI ^sN PERSONALIZADA DE NGINX
COPY nginx.conf /etc/nginx/conf.d/default.conf

EXPOSE 80