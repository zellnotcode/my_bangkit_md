package com.zell.intermediatesubmission

import com.zell.intermediatesubmission.response.ListStoryItem
import com.zell.intermediatesubmission.response.StoriesResponse

object DataDummy {

    fun generateDummyStoriesResponse() : List<ListStoryItem> {
        val items: MutableList<ListStoryItem> = arrayListOf()
        for (i in 0..100) {
            val story = ListStoryItem(
                "www.link.com/$i",
                "created at $i",
                i.toString(),
                i.toString(),
                id = i.toString()
            )
            items.add(story)
        }
        return items
    }
}