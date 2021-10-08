# Propeller - Hazelcast Client Server - Project X March 2021 

You need to have a k8 cluster running.

## Install

1. Start the cluster 

```   
kubectl apply -f hazelcast-cluster.yaml
```
    
2. Start the client
   
```
kubectl apply -f hazelcast-client.yaml
```
   
3. You can access the client using the SpringBoot service
   
```
$ kubectl get svc springboot-service
NAME                 TYPE        CLUSTER-IP   EXTERNAL-IP   PORT(S)   AGE
springboot-service   ClusterIP   10.4.9.105   <none>        80/TCP    18m
```