/*   
 * This file is part of PPiMapBuilder.
 *
 * PPiMapBuilder is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * PPiMapBuilder is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with PPiMapBuilder.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Copyright 2015 Echeverria P.C., Dupuis P., Cornut G., Gravouil K., Kieffer A., Picard D.
 * 
 */    	
    
package ch.picard.ppimapbuilder.data.protein.ortholog;


import ch.picard.ppimapbuilder.data.protein.ortholog.client.ProteinOrthologWebCachedClientTest;
import ch.picard.ppimapbuilder.data.protein.ortholog.client.cache.PMBProteinOrthologCacheClientLoadedTest;
import ch.picard.ppimapbuilder.data.protein.ortholog.client.cache.PMBProteinOrthologCacheClientTest;
import ch.picard.ppimapbuilder.data.protein.ortholog.client.cache.SpeciesPairProteinOrthologCacheTest;
import ch.picard.ppimapbuilder.data.protein.ortholog.client.cache.loader.InParanoidCacheLoaderTaskFactoryTest;
import ch.picard.ppimapbuilder.data.protein.ortholog.client.web.InParanoidClientTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({OrthologGroupTest.class, ProteinOrthologWebCachedClientTest.class, InParanoidClientTest.class,
		PMBProteinOrthologCacheClientTest.class, PMBProteinOrthologCacheClientLoadedTest.class,
		SpeciesPairProteinOrthologCacheTest.class, InParanoidCacheLoaderTaskFactoryTest.class})
public class OrthologyTestSuite {
}

