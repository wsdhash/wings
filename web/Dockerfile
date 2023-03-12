FROM python:3.9-alpine
WORKDIR /app
COPY index.html .
COPY signup.html .
EXPOSE 8000
CMD ["python3", "-m", "http.server", "8000"]