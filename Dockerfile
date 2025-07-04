# Etapa de build con Node.js
FROM node:20.9.0-alpine AS builder

WORKDIR /app

# Copiar dependencias e instalarlas
COPY package*.json ./
RUN npm ci

# Copiar el resto del código y construir
COPY . .
RUN npm run build

# Verificar contenido de dist
RUN ls -la dist && echo "Contenido de dist:" && cd dist && ls -la

# Etapa final con NGINX
FROM nginx:alpine

# Copiar configuración personalizada de NGINX
COPY nginx.conf /etc/nginx/nginx.conf

# Copiar el contenido compilado al servidor web
COPY --from=builder /app/dist/copichat /usr/share/nginx/html

EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]
