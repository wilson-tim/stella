package uk.co.firstchoice.viking.gui;

//import java.sql.*;
//import javax.sql.*;
//import javax.naming.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import uk.co.firstchoice.fcutil.FCException;
import uk.co.firstchoice.viking.gui.util.ForwardConstants;
import uk.co.firstchoice.viking.persist.VikingJDBC;

public class VersionAction extends Action {

	/**
	 * Process the specified HTTP request, and create the corresponding HTTP
	 * response (or forward to another web component that will create it).
	 * Return an <code>ActionForward</code> instance describing where and how
	 * control should be forwarded, or <code>null</code> if the response has
	 * already been completed.
	 * 
	 * @param mapping
	 *            The ActionMapping used to select this instance
	 * @param actionForm
	 *            The optional ActionForm bean for this request (if any)
	 * @param request
	 *            The HTTP request we are processing
	 * @param response
	 *            The HTTP response we are creating
	 * 
	 * @exception Exception
	 *                if the application business logic throws an exception
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		VersionFormBean vfb = (VersionFormBean) form;
		VikingJDBC vJdbc = new VikingJDBC();
		String resp = "";
		try {
			boolean reloadSession = true;
			if (vfb.getAction().equals("live")) {
				resp = vJdbc.versionSetLive(vfb.getLiveVersion(),
						cleanSeason(vfb.getLiveSeason()), request
								.getRemoteUser());
				if (null == resp || !resp.startsWith("Error")) {
					resp = "Live version set";
				}
			} else if (vfb.getAction().equals("default")) {
				resp = vJdbc.versionSetDefault(vfb.getDefaultVersion(),
						cleanSeason(vfb.getDefaultSeason()), request
								.getRemoteUser());
				if (null == resp || !resp.startsWith("Error")) {
					resp = "Default version set";
				}
			} else if (vfb.getAction().equals("create")) {
				resp = vJdbc.versionCreate(vfb.getNewVersion(), cleanSeason(vfb
						.getNewSeason()), request.getRemoteUser());
				if (null == resp || !resp.startsWith("Error")) {
					resp = "Version created";
				}
			} else if (vfb.getAction().equals("copy")) {
				resp = vJdbc.versionCopy(vfb.getCopyFromVersion(),
						cleanSeason(vfb.getCopyToSeason()), vfb
								.getCopyToVersion(), request.getRemoteUser());
				if (null == resp || !resp.startsWith("Error")) {
					resp = "Version copied";
				}
			} else if (vfb.getAction().equals("rename")) {
				resp = vJdbc.versionRename(vfb.getRenameFromVersion(),
						cleanSeason(vfb.getRenameToSeason()), vfb
								.getRenameToVersion(), request.getRemoteUser());
				if (null == resp || !resp.startsWith("Error")) {
					resp = "Version renamed";
				}
			} else if (vfb.getAction().equals("delete")) {
				resp = vJdbc.versionDelete(vfb.getDeleteVersion(), request
						.getRemoteUser());
				if (null == resp || !resp.startsWith("Error")) {
					resp = "Version deleted";
				}
			} else {
				reloadSession = false;
			}
			if (reloadSession) {
				request.getSession().setAttribute(
						ForwardConstants.SESSION_DATA, new SessionData());
			}
		} catch (FCException e) {
			e.printStackTrace();
			resp = e.getMessage();
		}
		vJdbc.close();
		vfb.reset(mapping, request);
		vfb.setError(resp);
		return mapping.findForward(ForwardConstants.SUCCESS);

	}

	private String cleanSeason(String season) {
		return (season + "; ").split(";")[0];
	}

}