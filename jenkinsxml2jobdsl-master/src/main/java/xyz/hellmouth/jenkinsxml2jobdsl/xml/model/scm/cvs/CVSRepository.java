package xyz.hellmouth.jenkinsxml2jobdsl.xml.model.scm.cvs;

public class CVSRepository {

	public String cvsRoot;
	public CVSRepositoryItems repositoryItems;
	public long compressionLevel;
	public CVSExcludedRegions excludedRegions;
	public String password;
	public boolean passwordRequired;
	public CVSRepositoryBrowser repositoryBrowser;
}
