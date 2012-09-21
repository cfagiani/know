know
====

A personal knowledge manager that provides the ability to tag and index content. 


The planned set of functionality is:
* ability to index multiple types of files (documents as well as other files that contain searchable metadata)
* ability to index files stored on the cloud (adapters for Google Drive, Dropbox, S3)
* plain-text and tag-base search
* tag management (merge, find unused, etc)
* command-line and visual interface


The codebase is organized into the following modules:
* know-core - contains the domain model and the business services for management of documents and tags
* know-engine - code for orchestrating the business service calls and performing index operations
* know-resource - code for finding and parsing files
* know-ui - user interface for the system. This module also contains the system entry points (i.e. main methods)
* know-webapp - restful service layer on top of the business services and engine.