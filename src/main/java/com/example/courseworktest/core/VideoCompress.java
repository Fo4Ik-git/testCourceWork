package com.example.courseworktest.core;

import com.example.courseworktest.config.VideoUploadHandler;
import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFmpegExecutor;
import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.builder.FFmpegBuilder;
import net.bramp.ffmpeg.job.FFmpegJob;
import net.bramp.ffmpeg.probe.FFmpegProbeResult;
import net.bramp.ffmpeg.progress.Progress;
import net.bramp.ffmpeg.progress.ProgressListener;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.TimeUnit;


public class VideoCompress implements VideoCompressor {

    private final VideoUploadHandler videoUploadHandler;

    public VideoCompress(VideoUploadHandler videoUploadHandler) {
        this.videoUploadHandler = videoUploadHandler;
    }


    private Path compressVideo(MultipartFile video, String uploadId, String outputVideoPath) {
        try {
            FFmpeg ffmpeg = new FFmpeg("C:\\ProgramData\\chocolatey\\lib\\ffmpeg\\tools\\ffmpeg\\bin\\ffmpeg.exe");
            FFprobe ffprobe = new FFprobe("C:\\ProgramData\\chocolatey\\lib\\ffmpeg\\tools\\ffmpeg\\bin\\ffprobe.exe");

            FFmpegExecutor executor = new FFmpegExecutor(ffmpeg, ffprobe);
            String inputVideoPath = video.getOriginalFilename();

            Files.write(Path.of(inputVideoPath), video.getBytes());

            FFmpegProbeResult in = ffprobe.probe(inputVideoPath);

            FFmpegBuilder builder = new FFmpegBuilder()
                    .setInput(in)
                    .addOutput(outputVideoPath)
                    .setAudioCodec("aac")
                    .setVideoCodec("libx265")
                    .setFormat("mp4")
                    .setVideoFrameRate(FFmpeg.FPS_24)
                    .setAudioChannels(FFmpeg.AUDIO_STEREO)
                    .setStrict(FFmpegBuilder.Strict.EXPERIMENTAL)
                    .addExtraArgs("-tile-columns", "6")
                    .done();

            FFmpegJob job = executor.createJob(builder, new ProgressListener() {
                final double duration_ns = in.getFormat().duration * TimeUnit.SECONDS.toNanos(1);

                @Override
                public void progress(Progress progress) {
                    int percentage = (int) (progress.out_time_ns / duration_ns * 100);
                    try {
                        videoUploadHandler.sendProgress(percentage, uploadId);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            });

            job.run();
            Files.delete(Path.of(inputVideoPath));
            return Path.of(outputVideoPath);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error: " + e.getMessage());
            return null;
        }
    }

    @Override
    public Path compress(MultipartFile video, String userSessionId, String outputVideoPath) throws InterruptedException {
        return compressVideo(video, userSessionId, outputVideoPath);
    }
}
