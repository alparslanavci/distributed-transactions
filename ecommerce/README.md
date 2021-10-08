# Propeller - E-commerce app - Distributed Transactions in a Service Mesh

You need to have a k8s cluster running with Istio installed and auto-injection enabled. Please check [here](https://istio.io/latest/docs/setup/getting-started/) for Istio installation details.

## Demo

1. Start the services 

```   
kubectl apply -f kubernetes.yaml
```

2. Get the external IP of the service

```
$ kubectl get svc ecommerce-service
NAME                TYPE           CLUSTER-IP       EXTERNAL-IP     PORT(S)          AGE
ecommerce-service   LoadBalancer   10.251.255.9     35.202.214.95   8081:30012/TCP   2m52s
```

3. Go to the following address in your browser to access the app

```
http://35.202.214.95:8081/
```

4. Try to purchase the item, observe the dual write probem after the error

5. Install Propeller Envoy filter

```
kubectl apply -f propeller-filter.yaml
```

6. Try to purchase the item again. This time, the saga should compensate the updates.
