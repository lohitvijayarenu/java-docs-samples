package com.google.cloud.storage.storagetransfer.samples;

import java.util.logging.Logger;
import java.io.IOException;
import com.google.api.services.storagetransfer.v1.Storagetransfer;
import com.google.api.services.storagetransfer.v1.model.ListOperationsResponse;
import com.google.api.services.storagetransfer.v1.model.ListTransferJobsResponse;
import com.google.api.services.storagetransfer.v1.model.TransferJob;

import org.apache.commons.cli.*;
import org.apache.commons.cli.ParseException;

public class STSJobStatus {

  private static final Logger LOG = Logger.getLogger(STSJobStatus.class.getName());
  private Storagetransfer storageClient = null;

  void checkStatus(CommandLine line) throws IOException {

    String projectName = line.getOptionValue("projectName");
    storageClient = TransferClientCreator.createStorageTransferClient();
    ListTransferJobsResponse response = storageClient.transferJobs()
          .list().setFilter("{\"project_id\": \"" + projectName + "\"}").execute();

    for (TransferJob job : response.getTransferJobs()) {
      System.out.println(job.getTransferSpec().getGcsDataSource()
          + " " + job.getName()
          + " " + job.getLastModificationTime()
          + " " + job.getStatus());
    }

    //LOG.info(response.toPrettyString());
  }

  private static CommandLine parseOptions(String[] args) {
    Option projectName = OptionBuilder.withArgName("projectName")
        .hasArg()
        .withDescription("Name of Google Project")
        .create("projectName");

    Options options = new Options();
    options.addOption(projectName);

    CommandLineParser parser = new DefaultParser();
    CommandLine line = null;
    try {
      line = parser.parse(options, args);
      System.out.println("Option" + line.getOptionValue("projectName"));
    } catch (ParseException  exp) {
      System.err.println("Parsing failed " + exp.getMessage());
    }
    return line;
  }

  public static void main(String[] args) throws IOException {

    CommandLine line = parseOptions(args);
    STSJobStatus stsJobStatus = new STSJobStatus();
    stsJobStatus.checkStatus(line);
   }
}
