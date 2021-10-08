# Propeller - Hazelcast Client Server - Distributed Transactions in a Service Mesh

You need to have a k8s cluster running.

## Install

1. Start the cluster 

```   
kubectl apply -f hazelcast-cluster.yaml
```
    
2. Start the propeller app
   
```
kubectl apply -f propeller-app.yaml
```
   
3. You can access the client using the propeller-app
   
```
$ kubectl get svc propeller-app
NAME                 TYPE        CLUSTER-IP   EXTERNAL-IP   PORT(S)   AGE
propeller-app        ClusterIP   10.4.9.105   <none>        80/TCP    18m
```