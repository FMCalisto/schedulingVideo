# Scheduling Repeating Video

Video give you a way to perform video-based operations outside the lifetime of your application. For example, you could use a video play to initiate a long-running operation, such as starting a service on presentation mode.

Video have these characteristics:

* They let you fire Intents at set videos and/or presentation files;

* You can use them in conjunction with videocast receivers to start services and perform other operations;

* They operate outside of your application, so you can use them to trigger events or actions even when your app is not running, and even if the device itself is asleep;

* They help you to minimize your app's resource requirements. You can schedule operations without relying on videos or continuously running background services;

> Note: For video operations that are guaranteed to occur during the lifetime of your application, instead consider using the Handler class in conjunction with Timer and Thread. This approach gives Android better control over system resources.
