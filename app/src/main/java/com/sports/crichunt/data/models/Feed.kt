package com.sports.crichunt.data.models

import org.simpleframework.xml.*

@Root(name = "item", strict = false)
data class Article @JvmOverloads constructor(
    @field:Element(name = "title")
    @param:Element(name = "title")
    val title: String? = null,
    @field:Element(name = "description")
    @param:Element(name = "description")
    val description: String? = null,
    @field:Element(name = "pubDate")
    @param:Element(name = "pubDate")
    val pubDate: String? = null,
    @field:Path("content")
    @param:Path("content")
    @field:Attribute(name = "url", required = false)
    @param:Attribute(name = "url", required = false)
    val image: String? = null
)

@Root(name = "rss", strict = false)
data class Feed @JvmOverloads constructor(
    @field:ElementList(name = "item", inline = true, required = false)
    @param:ElementList(name = "item", inline = true, required = false)
    @field:Path("channel")
    @param:Path("channel")
    var articleList: List<Article>? =
        null
)