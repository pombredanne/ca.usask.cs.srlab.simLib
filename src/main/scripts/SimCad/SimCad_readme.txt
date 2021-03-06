SimCad Clone Detector
---------------------
Version 	: 2.1
Released on	: August 2012
Supported OS	: Mac OS X, Linux.
Technical Ref. 	: http://dx.doi.org/10.1109/WCRE.2011.12
Origin 		: Software Research Lab., Dept. of Computer Science, University of Saskatchewan, Canada

Disclaimer
----------
The information is provided "as is", without warranty of any kind, whether expressed or implied.
Please use ALL information, commands and configuration with care and at your OWN sole responsibility.
The Authors' will not be responsible for any damages or loss of any kind resulting from its use.

What's new in SimCad-2.1
----------------------
# added new optional command-line parameter "item_to_search". This parameter takes a file/folder that contains source code to search for similar code in a target project. i.e., the detection behaves like a code search. When it is ignored, all clones inside the target project will be searched.
# added detection from source in pre-defined xml file of the following format. See instructions at step 5 in next section for command details. 
Please remember to use '-n' for non-exclusive fragments e.g, blocks.

   <source file=".../file-1.x" startline="x" 
		endline="y">
	fragment 1 text 
   </source>
   ...
   <source file=".../file-n.x" startline="x" 
		endline="y">
	fragment n text
   </source>


Installation Steps
------------------

1. Install java 1.5 or later (http://java.sun.com/javase/downloads/index.jsp)
	1.1 Check java installation
		$ java -version 

2. Install TXL 10.5i or later (http://www.txl.ca)
	2.1 Check TXL installation
		$ txl -V

3. Install SimCad
	3.1 Extract the archive
		$ cd PATH_CONTAINING_SimCad-2.1.zip
		$ unzip SimCad-2.1.zip
	3.2 $ cd SimCad-2.1
	3.3 $ make 
	3.4 Check SimCad installation
		$ ./simcad2 -version

4. Run SimCad
	4.1 $ cd PATH_CONTAINING_SimCad-2.1/SimCad-2.1
	4.2 $ ./simcad2 [-version] [-v] [-i item_to_search] -s source_path -l language
               [-g granularity] [-t clone_type]
               [-c clone_grouping] [-x source_transform]
               [-o output_path]          

		-version          = display simcad version
		-v                = verbose mode, shows the detection in progress
		language          = c | java | cs | py
		granularity       = (block | b ) | (function | f) : default = function
		clone_type        = 1 | 2 | 3 | 12 | (23 | nearmiss) | 13 | (123 | all) : default = 123
		clone_grouping    = (group | cg) | (pair | cp) : default = group
		source_transform  = (generous | g) | (greedy | G) : default = generous
		item_to_search    = absolute path to file/folder contains search item
		source_path       = absolute path to source folder
		output_path       = absolute path to output folder : default = {source_path}_simcad_clones
		
	4.3 example
		$ ./simcad2 -s /Users/foo/Documents/workspaces/my-project -l java
		
5. Run SimCadXML
	5.1 $ cd PATH_CONTAINING_SimCad-2.1/SimCad-2.1
	5.2 $ ./simcad2xml [-version] [-v] [-n] -s o_source_xml [-x t_source_xml]
                [-t clone_type] [-c clone_grouping] [-o output_path]
		
		-version          = display simcad version
		-v                = verbose mode, shows the detection in progress
		-n                = non-exclusive fragments, a fragment might contain in another bigger fragment (e.g: blocks)
		o_source_xml      = absolute path to xml file containing original source fragments
		t_source_xml      = absolute path to xml file containing transformed source fragments (optional)
		clone_type        = 1 | 2 | 3 | 12 | (23 | nearmiss) | 13 | (123 | all) : default = (123 | all)
		clone_grouping    = (group | cg) | (pair | cp) : default = (group | cg)
		output_path       = absolute path to output folder : default = {source_path}_simcad_clones			


Customize SimCad Configuration
------------------------------
SimCad uses the following list of configuration parameters that can be overridden by providing a similar entry with new value in the external configuration file "simcad.cfg.xml" located in folder SimCad-2.1/tools/.

<entry key="simcad.settings.advance.strictOnMembership">false</entry>
<entry key="simcad.settings.advance.clusterMembershipRatio">0.5</entry>
<entry key="simcad.settings.advance.locTolerance">1.0</entry>
<entry key="simcad.settings.advance.type3clone.simthreshold">13</entry>
<entry key="simcad.settings.advance.thresholdStabilizationValueForGreedyTransform">0.3</entry>
<entry key="simcad.settings.advance.tokenFrequencyNormalization">true</entry>
<entry key="simcad.settings.advance.tokenFrequencyNormalizationThreshold">5</entry>
<entry key="simcad.settings.advance.tokenFrequencyNormalizationOverThresholdValue">5</entry>
<entry key="simcad.settings.general.minClusterSize">2</entry>
<entry key="simcad.settings.general.minSizeOfGranularity">5</entry>
<entry key="simcad.settings.general.fragment.file.relativeURL">true</entry>
<entry key="simcad.settings.general.fragment.unicodeFilterOn">false</entry>
<entry key="simcad.settings.general.install.url">../../SimCad-2.0</entry>