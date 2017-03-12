# Todo
- Add ability to play audio and video files for a relevant word
- Add search menu
- When exploring roots, displays words that share that root

# Nice to haves
- Hide the configuration information in docker-compose.yml by using environment
variables that can be accessed in the docker-compose.yml file
    - Would also be nice if we could use this rather than the secret_config.edn
    file
- Deploy build to production after commit
    - Not going to bother with automated system testing for now
    - Need to research this more. Jenkins?
- Add ability to dev in docker container
  - This is not working well for the time being. File system is too slow in picking
  up code changes, resulting in a bad dev experience. Also, could not figure out
  how to connect to the repl in the container.
