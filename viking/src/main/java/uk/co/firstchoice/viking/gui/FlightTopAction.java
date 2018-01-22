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

import uk.co.firstchoice.fcutil.FCUtils;
import uk.co.firstchoice.viking.gui.util.ForwardConstants;
import uk.co.firstchoice.viking.persist.VikingJDBC;

public class FlightTopAction extends Action {

	private static final char COMMA = ',';

	private static final char SPEECH = '\'';

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

		FlightTopBean ftb = new FlightTopBean();
		VikingJDBC vJdbc = new VikingJDBC();
		String seriesNo = request.getParameter("seriesNo");
		//		HttpSession sess = request.getSession();
		//		if (((seriesNo == null) || (seriesNo.equals(""))) && (
		// sess.getAttribute("seriesNo") != null)) {
		//		     seriesNo = sess.getAttribute("seriesNo").toString();
		//		}
		//String version = request.getParameter("version");
		if (seriesNo != null) {
			ftb.setSeriesNo(seriesNo);
			getSeries(ftb, vJdbc);
			getDepartureInfo(ftb, vJdbc);
		}
		vJdbc.close();
		request.setAttribute("flighttopbean", ftb);
		return (mapping.findForward(ForwardConstants.SUCCESS));

	}

	private void getSeries(FlightTopBean ftb, VikingJDBC vJdbc) {
		String error = ForwardConstants.EMPTY;
		try {
			
System.out.print(" getSeries 1 ........");

			java.sql.ResultSet rset = vJdbc.getSeries(Long.parseLong(ftb
					.getSeriesNo()));
			if (rset.next()) {
				ftb.setStartDate(rset.getString("start_date"));
				ftb.setEndDate(rset.getString("end_date"));
				ftb.setWeekday(rset.getString("day_of_week"));
				String frq = rset.getString("frequency_code");
				//ftb.setFrequency("B".equals(frq) ? "F" : frq);
				ftb.setFrequency("F".equals(frq) ? "F" : frq); // B is changed to F(Fortnighly 
				ftb.setSeason(rset.getString("season"));
				ftb.setVersion(rset.getString("version_desc"));
				ftb.setLiveVersion("Y".equals(rset
						.getString("live_version_ind")));
				//ftb.setComments(rset.getString("comments"));
			} else {
				error = "Series not found";
			}
			vJdbc.closeResultSet(rset);
		} catch (Exception e) {
			error = e.getMessage();
		}
		ftb.setError(error);
	}

	private void getDepartureInfo(FlightTopBean ftb, VikingJDBC vJdbc) {
		try {
			java.sql.ResultSet rset = vJdbc.getSeriesDetailDepDate(Long
					.parseLong(ftb.getSeriesNo()));
			StringBuffer strBuf = new StringBuffer();
			boolean firstDate = true;
			while (rset.next()) {
				if (!firstDate) {
					strBuf.append(COMMA);
				}
				strBuf.append(SPEECH + rset.getString("dep_date") + SPEECH);
				firstDate = false;
			}
			ftb.setDepartureDates(strBuf.toString());

			/**
			 * [
			 * ['02/10/2003','16/10/2003','FF00','01','MAN','LPA','09:00','14:30','0','FCA332C','B757'],
			 * ['02/10/2003','16/10/2003','FF00','02','LPA','MAN','15:00','20:00','0','FCA332D','B757'] ]
			 */
			rset = vJdbc.getSeriesDetail(Long.parseLong(ftb.getSeriesNo()));

		    strBuf = new StringBuffer();
			firstDate = true;
			while (rset.next()) {
				if (!firstDate) {
					strBuf.append(COMMA);
				}
				strBuf.append("['" + rset.getString("min_departure_date")
						+ SPEECH + COMMA);
				strBuf.append(SPEECH + rset.getString("max_departure_date")
						+ SPEECH + COMMA);
				strBuf.append(SPEECH + rset.getString("colour_rgb") + SPEECH
						+ COMMA);
				strBuf.append(SPEECH + rset.getString("sequence_no") + SPEECH
						+ COMMA);
				strBuf.append(SPEECH
						+ FCUtils.notNull(rset.getString("gateway_code_from"))
						+ SPEECH + COMMA);
				strBuf.append(SPEECH
						+ FCUtils.notNull(rset.getString("gateway_code_to"))
						+ SPEECH + COMMA);
				strBuf
						.append(SPEECH
								+ ((rset.getString("sequence_no").equals("1") && FCUtils
										.notNull(
												rset
														.getString("departure_time"))
										.equals(ForwardConstants.EMPTY)) ? FCUtils
										.notNull(rset
												.getString("departure_slot"))
										: FCUtils.notNull(rset
												.getString("departure_time")))
								+ SPEECH + COMMA);
				strBuf.append(SPEECH
						+ FCUtils.notNull(rset.getString("arrival_time"))
						+ SPEECH + COMMA);
				strBuf.append(SPEECH
						+ FCUtils.notNull(rset.getString("overnight_period"))
						+ SPEECH + COMMA);
				strBuf.append(SPEECH
						+ FCUtils.notNull(rset.getString("flight_code"))
						+ FCUtils.notNull(rset.getString("flight_number"))
						+ SPEECH + COMMA);
				strBuf.append(SPEECH
						+ FCUtils.notNull(rset.getString("aircraft_type"))
						+ SPEECH + COMMA);
				
				strBuf.append(SPEECH
						+ FCUtils.notNull(rset.getString("flight_type_code"))
						+ SPEECH + COMMA);
				strBuf.append(SPEECH
						+ FCUtils.notNull(rset.getString("record_status"))
						+ "']");
				firstDate = false;
			}
			ftb.setDateGroupInfo(strBuf.toString());
			vJdbc.closeResultSet(rset);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}