def call(String student, String gkeIpName, String image, String dockerCred) {
    withCredentials([string(credentialsId: dockerCred, variable: 'dockerJson')]) {
        sh """cat <<EOF> manifest.yaml
apiVersion: v1
kind: Namespace
metadata:
  name: ${student}
---
apiVersion: v1
kind: Service
metadata:
  name: helloworld-service
  namespace: ${student}
spec:
  ports:
  - port: 80
    protocol: TCP
    targetPort: 8080
  selector:
    app: helloworld
  type: NodePort
---
apiVersion: apps/v1
kind: Deployment
metadata:
  labels:
    app: helloworld
  name: helloworld
  namespace: ${student}
spec:
  replicas: 1
  selector:
    matchLabels:
      app: helloworld
  template:
    metadata:
      labels:
        app: helloworld
    spec:
      containers:
      - image: ${image}
        imagePullPolicy: Always
        name: helloworld
        ports:
        - containerPort: 8080
          protocol: TCP
        readinessProbe:
          failureThreshold: 5
          httpGet:
            path: /
            port: 8080
            scheme: HTTP
          periodSeconds: 10
          successThreshold: 2
          timeoutSeconds: 5
      imagePullSecrets:
      - name: regcred
---
apiVersion: v1
kind: Secret
metadata:
  name: regcred
  namespace: ${student}
data:
  .dockerconfigjson: ${dockerJson}
type: kubernetes.io/dockerconfigjson
---
apiVersion: extensions/v1beta1
kind: Ingress
metadata:
  annotations:
    kubernetes.io/ingress.class: gce
    kubernetes.io/ingress.global-static-ip-name: ${gkeIpName}
  name: helloworld-ingress
  namespace: ${student}
spec:
  rules:
  - host: helloworld-${student}.${student}.lab.playpit.by
    http:
      paths:
      - backend:
          serviceName: helloworld-service
          servicePort: 80
        path: /*
        pathType: ImplementationSpecific
EOF"""
        sh 'chown jenkins:jenkins manifest.yaml'
    }
}