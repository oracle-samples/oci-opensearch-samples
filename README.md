

# OCI OpenSearch Service Code Samples

The OCI OpenSearch Service  has created this  public-facing repository to share sample codes with our customers, particularly for major releases. This presents an opportunity to better engage with our users and streamline adoption.

Over the past year, we have achieved remarkable milestones with the release of several key AI/ML and observability features that align with the industry's growing focus on Large Language Models (LLMs) and Retrieval Augmented Generation (RAG). Highlights of these releases include:

* **KNN Plugin**: Enabling semantic and hybrid search with support for various search engines such as Lucene, Faiss, and nmslib.
* **Ingestion Pipelines**: Automating the generation of document embeddings during ingestion.
* **Pretrained Model Support**: Seamlessly registering and deploying pretrained embedding models by simply specifying the model name.
* **BYOM (Bring Your Own Model)**: Allowing users to register and load custom pretrained models from an Object Storage Bucket.
* **GenAI Connector**: Providing access to hosted LLM models for chat, embeddings, text completion, and summarization, with support for dedicated model endpoints.
* **Data Science Connector**: Extending access to a wider range of LLM models vetted by the Oracle Data Science Team, with enhanced control over deploying and hosting traditional ML, CV, and LLM models.
* **RAG and Conversational Search**: Enabling users to perform RAG and conversational search by registering and deploying their LLM models in OpenSearch using GenAI or Data Science connectors.
* **Data Prepper**: Preparing for GA release to preprocess data from diverse sources for ingestion into OpenSearch.
* **Automated RAG Pipeline**: Simplifying the complexity of setting up RAG pipelines through a guided OpenSearch console tool, with auto-generated code available for download. 

<br><br>

With the upcoming 2.15 version release, we are excited to introduce new features, including:

* AI Agents
* Learn to Rank (LTR)
* Dashboard Conversational Assistant Agent
* Automated Data Chunking

<br>

## Our Vision
A major objective for our team is to address customer pain points, reduce adoption barriers, and accelerate the development of LLM-powered applications. To achieve this, we aim to:

* Share comprehensive code samples to reduce development overhead and costs.
* Promote new feature releases through engaging blog posts with accompanying code snippets.
* Establish a dedicated repository as the definitive resource for all current and future code samples.
* This repository will not only support our upcoming blog post and feature releases but also serve as a central hub for subsequent code contributions. By providing these resources, we anticipate a significant reduction in Jira tickets within our incident queue, ensuring a smoother and more efficient customer experience.

Through this initiative, we aim to foster greater awareness of our service and empower customers to leverage the full potential of OCI OpenSearch¿s cutting-edge capabilities.

<br><br>

## Installation
To get started, you can clone this repository with either one of the options below and navigate to the sub-folder that contains the use case of interest.

* SSH:
```shell
git clone git@github.com:oracle-samples/oci-opensearch-samples.git
``` 

* HTTPS:
```shell
git clone https://github.com/oracle-samples/oci-opensearch-samples.git
```


<br><br>

## Topics
- [OpenSearch Seemless Integration with LangChain and GenAI Service Leveraging the Oracle Accelerate Data Science NoteBook](./opensearch-integration-with-langchain)
    * **Use Cases**:
        * Semantic Search 
        * RAG/Conversational Search 


 More Code Samples to come...


<br>

## Documentation

Check out the following resources for more information about the OCI OpenSearch Service:

* [Create an OpenSearch Cluster](https://docs.oracle.com/en/learn/oci-opensearch/index.html#introduction)
* [Create Tuneling to Connect to Your OpenSearch Cluster from Local MaChine](https://docs.oracle.com/en/learn/oci-opensearch/index.html#option-2-from-your-local-machine-through-port-forwarding)
* [Semantic Search with OpenSearch Documentation](https://docs.oracle.com/en-us/iaas/Content/search-opensearch/Concepts/semanticsearch.htm)
* [How To Use OpenSearch Pre-trained Models](https://docs.oracle.com/en-us/iaas/Content/search-opensearch/Tasks/opensearchpretrainedmodelwalkthrough.htm)
* [Bring Your Own Pre-trained Transformer Model using Object Storage Bucket](https://docs.oracle.com/en-us/iaas/Content/search-opensearch/Concepts/opensearchbyom.htm)
* [OpenSearch Semantic Search Walk-through](https://docs.oracle.com/en-us/iaas/Content/search-opensearch/Tasks/semanticsearchwalkthrough.htm)
* [OpenSearch conversational Search Walk-Through](https://docs.oracle.com/en-us/iaas/Content/search-opensearch/Concepts/ocigenaiconnector.htm)
* [OpenSearch with Oracle Data Sience Connector](https://docs.oracle.com/en-us/iaas/Content/search-opensearch/Concepts/datascienceconnector.htm)
* [Conversational Search Walk Trhough with OCI GenAI Connector](https://docs.oracle.com/en-us/iaas/Content/search-opensearch/Concepts/conversationalsearchwalkthrough.htm)

<br>

## Contributing
This project welcomes contributions from the community. Before submitting a pull request, please [review our contribution guide](./CONTRIBUTING.md)

<br>

## Security
Please consult the [security guide](./SECURITY.md) for our responsible security vulnerability disclosure process

<br>

## Help

For help please create a GitHub [issue](https://github.com/oracle-samples/oci-opensearch-samples/issues). 
Be sure to provide all the necessary details so we can help you faster. If facing bugs, please provide the steps followed and codes samples for reproducing the bugs. 

<br>

## License

Copyright (c) 2024, 2025 Oracle and/or its affiliates.

Released under the Universal Permissive License v1.0 as shown at
<https://oss.oracle.com/licenses/upl/>. 

[LICENSE](./LICENSE.txt)


