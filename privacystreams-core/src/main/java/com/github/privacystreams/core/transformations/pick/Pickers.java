package com.github.privacystreams.core.transformations.pick;

import com.github.privacystreams.core.Function;
import com.github.privacystreams.core.MultiItemStream;
import com.github.privacystreams.core.SingleItemStream;
import com.github.privacystreams.utils.annotations.PSOperatorWrapper;

/**
 * A helper class to access picker functions
 */
@PSOperatorWrapper
public class Pickers {
    /**
     * Pick an item from a stream.
     *
     * @param index the index of the item to pick.
     * @return the stream mapper function.
     */
    public static Function<MultiItemStream, SingleItemStream> pick(int index) {
        return new StreamItemPicker(index);
    }
}
