FROM nginx:1.15.10
COPY nginx.conf /etc/nginx/nginx.conf
COPY static-html-directory /usr/share/nginx/html
CMD ["nginx", "-g", "daemon off;"]
