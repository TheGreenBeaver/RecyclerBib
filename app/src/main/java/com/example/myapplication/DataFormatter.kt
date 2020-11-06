package com.example.myapplication

import name.ank.lab4.BibEntry
import name.ank.lab4.Keys
import name.ank.lab4.Types

data class FormattedData(
    val title: String,
    val authors: String,
    val publishInfo: String,
    val pages: String?,
    val url: String?,

    val displayType: DisplayType,

    var source: String?
)

enum class DisplayType {
    FRAGMENT, INDEPENDENT, UNPUBLISHED, DEFAULT
}

fun getDisplayType(type: Types): DisplayType = when (type) {
    in arrayOf(Types.BOOK, Types.TECHREPORT) -> DisplayType.INDEPENDENT
    Types.UNPUBLISHED -> DisplayType.UNPUBLISHED
    in arrayOf(Types.ARTICLE, Types.MISC, Types.INCOLLECTION, Types.INPROCEEDINGS) -> DisplayType.FRAGMENT
    else -> DisplayType.DEFAULT
}

fun cutString(str: String, cutTo: Int = 35) =
    if (str.length <= cutTo) str else str.substring(0, cutTo - 2) + "..."

fun getPublishInfo(dataEntry: BibEntry): String =
    if (dataEntry.type != Types.UNPUBLISHED) {
        val year = dataEntry.getField(Keys.YEAR)
        val publisher = dataEntry.getField(Keys.PUBLISHER)
        val address = dataEntry.getField(Keys.ADDRESS)

        val res = ArrayList<String>()
        if (publisher != null) {
            res.add(publisher)
        }
        if (address != null) {
            res.add(address)
        }
        if (year != null) {
            res.add(year)
        }

        var howPublished = ""
        if (dataEntry.type == Types.MISC) {
            val howPublishedRaw = dataEntry.getField(Keys.HOWPUBLISHED)
            if (howPublishedRaw != null) {
                howPublished = " ($howPublishedRaw)"
            }
        }

        if (res.isEmpty()) "No Info" else "${res.joinToString()}$howPublished"
    } else {
        val submittedIn = dataEntry.getField(Keys.YEAR)
        if (submittedIn == null) "No info" else "Submitted in ${submittedIn.split(", ")[0]}"
    }

fun getAuthors(authorsRaw: String?): String {
    if (authorsRaw == null) {
        return "Unknown author"
    }

    val splitAuthors = authorsRaw.split(" and ")
    val res = ArrayList<String>()
    var summaryLength = 0
    var tooLong = false
    for (idx in splitAuthors.indices) {
        if (idx > 4 || summaryLength >= 50) {
            tooLong = true
            break
        }
        val spl = splitAuthors[idx].split(", ")
        val newPart = "${spl[1]} ${spl[0]}"
        summaryLength += newPart.length
        res.add(newPart)
    }
    return "${res.joinToString()}${if (tooLong) " and others" else ""}"
}

fun getSource(dataEntry: BibEntry): String {
    val res = when (dataEntry.type) {
        Types.ARTICLE -> dataEntry.getField(Keys.JOURNAL)
        in arrayOf(
            Types.INCOLLECTION,
            Types.INPROCEEDINGS,
            Types.MISC
        ) -> dataEntry.getField(Keys.BOOKTITLE)
        else -> null
    }
    return if (res == null) "Unknown" else cutString(res)
}

fun getFormattedData(dataEntry: BibEntry): FormattedData {
    val type = dataEntry.type

    val displayType = getDisplayType(type)
    val title = dataEntry.getField(Keys.TITLE)?.let { cutString(it, 48) } ?: "**$type**"
    val authors = getAuthors(dataEntry.getField(Keys.AUTHOR))
    val publishInfo = getPublishInfo(dataEntry)
    val pages = dataEntry.getField(Keys.PAGES)
    val url = dataEntry.getField(Keys.URL)?.let { cutString(it) }

    val formattedData =
        FormattedData(title, authors, publishInfo, pages, url, displayType, null)

    if (displayType == DisplayType.FRAGMENT) {
        formattedData.source = getSource(dataEntry)
    }

    return formattedData
}