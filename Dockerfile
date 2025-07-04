# Etapa 1: build Angular
FROM node:20-alpine AS build

WORKDIR /app

# Copiar solo los archivos de dependencias primero
COPY package*.json ./
RUN npm install
COPY . .
RUN npm run build -- --configuration=production

# Etapa 2: servir con nginx
FROM nginx:alpine

#  ^|^e ELIMINAR ARCHIVOS POR DEFECTO DE NGINX
RUN rm -rf /usr/share/nginx/html/*

#  ^|^e COPIAR TU APLICACI ^sN ANGULAR (dist/.../browser)
COPY --from=build /app/dist/copichat/browser/ /usr/share/nginx/html/

#  ^|^e CONFIGURACI ^sN PERSONALIZADA DE NGINX
COPY nginx.conf /etc/nginx/conf.d/default.conf

EXPOSE 80

# Comando por defecto para correr Nginx
CMD ["nginx", "-g", "daemon off;"]
