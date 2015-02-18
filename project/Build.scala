/*
 * Copyright © 2014-2015 Typesafe, Inc. All rights reserved. No information contained herein may be reproduced or
 * transmitted in any form or by any means without the express written permission of Typesafe, Inc.
 */

import com.typesafe.sbt.SbtScalariform._
import de.heikoseeberger.sbtheader.SbtHeader.autoImport._
import sbtrelease.ReleasePlugin._
import sbt._
import sbt.Keys._
import scalariform.formatter.preferences._

object Build extends AutoPlugin {

  override def requires =
    plugins.JvmPlugin

  override def trigger =
    allRequirements

  override def projectSettings =
    scalariformSettings ++
    inConfig(Compile)(compileInputs.in(compile) <<= compileInputs.in(compile).dependsOn(createHeaders.in(compile))) ++
    inConfig(Test)(compileInputs.in(compile) <<= compileInputs.in(compile).dependsOn(createHeaders.in(compile))) ++
    releaseSettings ++
    List(
      // Core settings
      organization := "com.typesafe.conductr",
      scalaVersion := Version.scala,
      crossScalaVersions := List(scalaVersion.value),
      scalacOptions ++= List(
        "-unchecked",
        "-deprecation",
        "-feature",
        "-language:_",
        "-target:jvm-1.6",
        "-encoding", "UTF-8"
      ),
      unmanagedSourceDirectories in Compile := List((scalaSource in Compile).value),
      unmanagedSourceDirectories in Test := List((scalaSource in Test).value),
      // Scalariform settings
      ScalariformKeys.preferences := ScalariformKeys.preferences.value
        .setPreference(AlignSingleLineCaseStatements, true)
        .setPreference(AlignSingleLineCaseStatements.MaxArrowIndent, 100)
        .setPreference(DoubleIndentClassDeclaration, true)
        .setPreference(PreserveDanglingCloseParenthesis, true),
      // Header settings
      headers := Map(
        "scala" -> (
          HeaderPattern.cStyleBlockComment,
          """|/*
            | * Copyright © 2014-2015 Typesafe, Inc. All rights reserved.
            | * No information contained herein may be reproduced or transmitted in any form
            | * or by any means without the express written permission of Typesafe, Inc.
            | */
            |
            |""".stripMargin
          ),
        "py" -> (
          HeaderPattern.hashLineComment,
          """|# Copyright © 2014-2015 Typesafe, Inc. All rights reserved.
            |# No information contained herein may be reproduced or transmitted in any form
            |# or by any means without the express written permission of Typesafe, Inc.
            |
            |""".stripMargin
          ),
        "conf" -> (
          HeaderPattern.hashLineComment,
          """|# Copyright © 2014-2015 Typesafe, Inc. All rights reserved.
            |# No information contained herein may be reproduced or transmitted in any form
            |# or by any means without the express written permission of Typesafe, Inc.
            |
            |""".stripMargin
          )
      ),
      // Publishing
      publishTo := {
        val typesafe = "http://private-repo.typesafe.com/typesafe/"
        val (name, url) = if (isSnapshot.value)
          ("typesafe-snapshots", typesafe + "maven-snapshots")
        else
          ("typesafe-releases", typesafe + "maven-releases")
        Some(Resolver.url(name, new URL(url)))
      }
    )
}
