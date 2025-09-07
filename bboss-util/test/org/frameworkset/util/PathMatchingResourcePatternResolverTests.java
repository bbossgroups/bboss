package org.frameworkset.util;
/**
 * Copyright 2020 bboss
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


import com.frameworkset.util.SimpleStringUtil;
import org.assertj.core.api.ThrowableAssert;
import org.frameworkset.util.io.PathMatchingResourcePatternResolver;
import org.frameworkset.util.io.Resource;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

/**
 * <p>Description: </p>
 * <p></p>
 * <p>Copyright (c) 2020</p>
 * @Date 2020/6/26 13:55
 * @author biaoping.yin
 * @version 1.0
 */
public class PathMatchingResourcePatternResolverTests {
	private static final String[] CLASSES_IN_CORE_IO_SUPPORT =
			new String[] {"EncodedResource.class", "LocalizedResourceHelper.class",
					"PathMatchingResourcePatternResolver.class", "PropertiesLoaderSupport.class",
					"PropertiesLoaderUtils.class", "ResourceArrayPropertyEditor.class",
					"ResourcePatternResolver.class", "ResourcePatternUtils.class"};

	private static final String[] TEST_CLASSES_IN_CORE_IO_SUPPORT =
			new String[] {"PathMatchingResourcePatternResolverTests.class"};

	private static final String[] CLASSES_IN_REACTOR_UTIL_ANNOTATIONS =
			new String[] {"NonNull.class", "NonNullApi.class", "Nullable.class"};

	private PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();


	@Test
	public void invalidPrefixWithPatternElementInIt() throws IOException {
		assertThatExceptionOfType(FileNotFoundException.class).isThrownBy(new ThrowableAssert.ThrowingCallable() {
			@Override
			public void call() throws Throwable {
				resolver.getResources("xx**:**/*.xy");
			}
		});
	}

	@Test
	public void singleResourceOnFileSystem() throws IOException {
		Resource[] resources =
				resolver.getResources("org/frameworkset/util/PathMatchingResourcePatternResolverTests.class");
		assertThat(resources.length).isEqualTo(1);
		assertProtocolAndFilenames(resources, "file", "PathMatchingResourcePatternResolverTests.class");
	}

	@Test
	public void singleResourceInJar() throws IOException {
		Resource[] resources = resolver.getResources("org.junit.Test.class");
		assertThat(resources.length).isEqualTo(1);
		assertProtocolAndFilenames(resources, "jar", "Test.class");
	}

//	@Disabled
	@Test
public void classpathStarWithPatternOnFileSystem() throws IOException {
		Resource[] resources = resolver.getResources("classpath*:org/frameworkset/util*/*.class");
		// Have to exclude Clover-generated class files here,
		// as we might be running as part of a Clover test run.
		List<Resource> noCloverResources = new ArrayList<Resource>();
		for (Resource resource : resources) {
			if (!resource.getFilename().contains("$__CLOVER_")) {
				noCloverResources.add(resource);
			}
		}
		resources = noCloverResources.toArray(new Resource[0]);
		assertProtocolAndFilenames(resources, "file",
				SimpleStringUtil.concatenateStringArrays(CLASSES_IN_CORE_IO_SUPPORT, TEST_CLASSES_IN_CORE_IO_SUPPORT));
	}

	@Test
	public void getResourcesOnFileSystemContainingHashtagsInTheirFileNames() throws IOException {
		Resource[] resources = resolver.getResources("classpath*:mess*.properties");
		System.out.println(resources);
//		assertThat(resources).extracting(Resource::getFile).extracting(File::getName)
//				.containsExactlyInAnyOrder("resource#test1.txt", "resource#test2.txt");
	}

	@Test
	public void classpathWithPatternInJar() throws IOException {
		Resource[] resources = resolver.getResources("classpath:reactor/util/annotation/*.class");
		assertProtocolAndFilenames(resources, "jar", CLASSES_IN_REACTOR_UTIL_ANNOTATIONS);
	}

	@Test
	public void classpathStarWithPatternInJar() throws IOException {
		Resource[] resources = resolver.getResources("classpath*:reactor/util/annotation/*.class");
		assertProtocolAndFilenames(resources, "jar", CLASSES_IN_REACTOR_UTIL_ANNOTATIONS);
	}

	@Test
	public void rootPatternRetrievalInJarFiles() throws IOException {
		Resource[] resources = resolver.getResources("classpath*:*.dtd");
		boolean found = false;
		for (Resource resource : resources) {
			if (resource.getFilename().equals("aspectj_1_5_0.dtd")) {
				found = true;
				break;
			}
		}
//		assertThat(found).as("Could not find aspectj_1_5_0.dtd in the root of the aspectjweaver jar").isTrue();
	}


	private void assertProtocolAndFilenames(Resource[] resources, String protocol, String... filenames)
			throws IOException {

		// Uncomment the following if you encounter problems with matching against the file system
		// It shows file locations.
//		String[] actualNames = new String[resources.length];
//		for (int i = 0; i < resources.length; i++) {
//			actualNames[i] = resources[i].getFilename();
//		}
//		List sortedActualNames = new LinkedList(Arrays.asList(actualNames));
//		List expectedNames = new LinkedList(Arrays.asList(fileNames));
//		Collections.sort(sortedActualNames);
//		Collections.sort(expectedNames);
//
//		System.out.println("-----------");
//		System.out.println("Expected: " + StringUtils.collectionToCommaDelimitedString(expectedNames));
//		System.out.println("Actual: " + StringUtils.collectionToCommaDelimitedString(sortedActualNames));
//		for (int i = 0; i < resources.length; i++) {
//			System.out.println(resources[i]);
//		}

//		assertThat(resources.length).as("Correct number of files found").isEqualTo(filenames.length);
		for (Resource resource : resources) {
			String actualProtocol = resource.getURL().getProtocol();
//			assertThat(actualProtocol).isEqualTo(protocol);
			assertFilenameIn(resource, filenames);
		}
	}

	private void assertFilenameIn(Resource resource, String... filenames) {
		String filename = resource.getFilename();
//		assertThat(Arrays.stream(filenames).anyMatch(filename::endsWith)).as(resource + " does not have a filename that matches any of the specified names").isTrue();
	}
}
