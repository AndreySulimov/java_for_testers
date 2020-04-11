package ru.jft.github;

import com.google.common.collect.ImmutableMap;
import com.jcabi.github.*;
import org.testng.annotations.Test;

import java.io.IOException;

public class GithubTests {

  @Test
  public void testCommits() throws IOException {
    Github github = new RtGithub("c3afe41c4856c1baf3b26e8d24018dd2d3f4aa61");
    RepoCommits commits = github.repos().get(new Coordinates.Simple("AndreySulimov", "java_for_testers")).commits();
    for (RepoCommit commit : commits.iterate(new ImmutableMap.Builder<String, String>().build())) {
      System.out.println(new RepoCommit.Smart(commit).message());
    }
  }
}