package com.example.courseworktest;

import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFmpegExecutor;
import net.bramp.ffmpeg.FFmpegUtils;
import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.builder.FFmpegBuilder;
import net.bramp.ffmpeg.job.FFmpegJob;
import net.bramp.ffmpeg.probe.FFmpegProbeResult;
import net.bramp.ffmpeg.progress.Progress;
import net.bramp.ffmpeg.progress.ProgressListener;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.TimeUnit;

public class CompressVideo {

    public static Path compressVideo(MultipartFile video) {
        try {
            FFmpeg ffmpeg = new FFmpeg("C:\\ProgramData\\chocolatey\\lib\\ffmpeg\\tools\\ffmpeg\\bin\\ffmpeg.exe");
            FFprobe ffprobe = new FFprobe("C:\\ProgramData\\chocolatey\\lib\\ffmpeg\\tools\\ffmpeg\\bin\\ffprobe.exe");

            FFmpegExecutor executor = new FFmpegExecutor(ffmpeg, ffprobe);
            String outputVideoPath = "output.mp4";
            String inputVideoPath = video.getOriginalFilename();

            Files.write(Path.of(inputVideoPath), video.getBytes());

            FFmpegProbeResult in = ffprobe.probe(inputVideoPath);

            //25 mb 586 sec
            /*FFmpegBuilder builder = new FFmpegBuilder()
                    .setInput(in) // Or filename
                    .addOutput(outputVideoPath)
                    .setAudioCodec("libvorbis")
                    .setVideoCodec("libvpx-vp9")
                    .setFormat("webm")
                    .setVideoFrameRate(FFmpeg.FPS_24)
                    .setAudioChannels(FFmpeg.AUDIO_STEREO)
                    .setStrict(FFmpegBuilder.Strict.EXPERIMENTAL)
//                    .addExtraArgs("-threads", "4")
//                    .addExtraArgs("-tile-columns", "6")
                    .addExtraArgs("-speed", "3")
                    .addExtraArgs("-quality", "good")
                    .addExtraArgs("-crf", "33")
                    .addExtraArgs("-b:v", "1000k")
                    .done();*/

            //75 mb 226 sec
            /*FFmpegBuilder builder = new FFmpegBuilder()
                    .setInput(in)
                    .addOutput(outputVideoPath)
                    .setAudioCodec("aac")
                    .setVideoCodec("libx264")
                    .setFormat("mp4")
                    .setVideoFrameRate(FFmpeg.FPS_24)
                    .setAudioChannels(FFmpeg.AUDIO_STEREO)
                    .setStrict(FFmpegBuilder.Strict.EXPERIMENTAL)
                    .addExtraArgs("-tile-columns", "6")
                    .done();*/

            // 25 mb 374 sec
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

                // Using the FFmpegProbeResult determine the duration of the input
                final double duration_ns = in.getFormat().duration * TimeUnit.SECONDS.toNanos(1);

                @Override
                public void progress(Progress progress) {
                    double percentage = progress.out_time_ns / duration_ns;

                    // Print out interesting information about the progress
                    System.out.println(String.format(
                            "[%.0f%%] status:%s frame:%d time:%s ms fps:%.0f speed:%.2fx",
                            percentage * 100,
                            progress.status,
                            progress.frame,
                            FFmpegUtils.toTimecode(progress.out_time_ns, TimeUnit.NANOSECONDS),
                            progress.fps.doubleValue(),
                            progress.speed
                    ));
                }
            });

            job.run();
            Files.delete(Path.of(inputVideoPath));
            return Path.of(outputVideoPath);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public static void main(String[] args) {
        //Count time of execution
        long startTime = System.currentTimeMillis();
        String inputVideoPath = "nc.mp4"; // Replace with your input video file
        String outputVideoPath = "nc1.mp4"; // Replace with your output video file

//        compressVideo(inputVideoPath, outputVideoPath);

        // Construct the FFmpeg command
        long endTime = System.currentTimeMillis();
        long executionTime = endTime - startTime;
        System.out.println("Time execution: " + executionTime / 1000 + " seconds");

    }


}