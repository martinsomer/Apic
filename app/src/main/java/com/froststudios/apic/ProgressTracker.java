package com.froststudios.apic;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.BufferedSink;
import okio.Okio;

public class ProgressTracker extends RequestBody {
    private ByteArrayInputStream source;
    private MediaType contentType;
    private long byteCount;
    private ProgressListener listener;

    ProgressTracker(final MediaType type, final ByteArrayOutputStream bytes, final ProgressListener listener) {
        this.contentType = type;
        this.source = new ByteArrayInputStream(bytes.toByteArray());
        this.byteCount = source.available();
        this.listener = listener;
    }

    @Override
    public MediaType contentType() {
        return contentType;
    }

    @Override
    public long contentLength() {
        return byteCount;
    }

    @Override
    public void writeTo(BufferedSink sink) throws IOException {
        long read;
        long total = 0;
        okio.Source reader = Okio.source(source);

        while ((read = reader.read(sink.buffer(), 1024)) != -1) {
            total += read;
            sink.flush();
            listener.transferred(total, byteCount);
        }
    }

    public interface ProgressListener {
        void transferred(long total, long length);
    }
}
