package net.benelog.blog.migration.etl

import javax.annotation.PostConstruct
import org.springframework.batch.item.ItemReader

abstract class PagingItemReader<T> : ItemReader<T> {
    lateinit var itemReader: ItemReader<T>

    override fun read(): T? {
        var item: T? = itemReader.read()
        if (item == null) {
            itemReader = nextItemReader()
            return itemReader.read()
        }
        return item
    }

    abstract fun nextItemReader(): ItemReader<T>

    @PostConstruct
    public fun initReader() {
        itemReader = nextItemReader()
    }
}
