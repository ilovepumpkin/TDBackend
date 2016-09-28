package testdash.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;

import testdash.exception.TestDashException;
import testdash.model.TeamContext;

public class TeamContextLoader {
	private Properties teamContextProps;

	public TeamContextLoader() {
		teamContextProps = new Properties();
		try {
			InputStream is = this.getClass().getClassLoader()
					.getResourceAsStream("teamcontext.properties");
			teamContextProps.load(is);
		} catch (IOException e) {
			throw new TestDashException(
					"Fail to load team context properties file", e);
		}
	}

	public Map getTeamContextMap() {
		Map teamContextMap = new HashMap();
		String teams = teamContextProps.getProperty("teams");
		StringTokenizer st = new StringTokenizer(teams, ",");
		while (st.hasMoreTokens()) {
			String team = st.nextToken();
			String fullName = teamContextProps.getProperty(team + ".fullName");

			String erSCurveFilePath = teamContextProps.getProperty(team
					+ ".erSCurveFilePath");
			String defectSCurveFilePath = teamContextProps.getProperty(team
					+ ".defectSCurveFilePath");

			String erStatusProviderClass = teamContextProps.getProperty(team
					+ ".erStatusProviderClass");
			String bugDataProviderClass = teamContextProps.getProperty(team
					+ ".bugDataProviderClass");
			String extraPropsFile = teamContextProps.getProperty(team
					+ ".extraPropsFile");
			TeamContext tc = new TeamContext(fullName, erSCurveFilePath,
					defectSCurveFilePath, erStatusProviderClass,
					bugDataProviderClass);

			if (extraPropsFile != null) {
				Properties extraProps = new Properties();
				try {
					InputStream is = this.getClass().getClassLoader()
							.getResourceAsStream(extraPropsFile);
					extraProps.load(is);
					tc.setExtraProperties(extraProps);
				} catch (IOException e) {
					throw new TestDashException(
							"Fail to load extra properties file for "
									+ fullName, e);
				}
			}

			teamContextMap.put(team, tc);
		}
		return teamContextMap;
	}
}
