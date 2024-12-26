# Kotlin No-Arg Plugin

Quote: 

> The no-arg compiler plugin generates an additional zero-argument constructor for classes with a specific annotation. 
> The generated constructor is synthetic, so it can't be directly called from Java or Kotlin, but it can be called using reflection. 
> This allows the Java Persistence API (JPA) to instantiate a class, although it doesn't have the zero-parameter constructor from 
> Kotlin or Java point of view (see the description of kotlin-jpa plugin below).
