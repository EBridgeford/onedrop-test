1. For this project I utilized
   1. Spring-Boot (http web server, helper libraries, many annotations)
   2. Lombok (code generation, automate conversion to and from JSON)
   3. Caffeine Cache system
2. Trade-Offs
   1. I initially started writing this assignment in Scala and Play Framework since this tech stack is utilized by OneDrop. However learning a new language and framework ended up being too large of a task so I fell back to Spring-Boot which I am familiar with. The concepts in Scala and Play were interesting and I look forward to revisiting them.
   2. The URL generation process is very basic to save time. A thorough reading of the API documentation and spending some extra time I could utilize tools generate the URL in a more durable and reliable way.
   3. I did not utilize async functionality so http requests are done sequentially. So situations where a large number of queries are done would run very slowly right now.
   4. Because of the small scope of this task a basic in memory cache was used. This caching strategy is fast but has a major downside when deployed in a distributed environment. Each application can have slightly different cache data; different cache results, that will expire at different times, etc. Using a networked solution like redis would be more correct.
   5. REST endpoints could be expanded. For the sake of time they are all GET endpoints that accept a comma separated list of values. A POST endpoint could also be added.
3. Future Work
   1. Rewrite loops to utilize more functional programming practices
   2. Improve testing
   3. Better utilize annotations like @NotNull or @NotEmpty