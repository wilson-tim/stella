package uk.co.firstchoice.viking.gui;

import java.sql.ResultSet;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import uk.co.firstchoice.fcutil.FCException;
import uk.co.firstchoice.fcutil.FCUtils;
import uk.co.firstchoice.viking.gui.util.ForwardConstants;
import uk.co.firstchoice.viking.persist.VikingJDBC;

public class FFAAction extends Action {

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

		VikingJDBC vJdbc = new VikingJDBC();
		FFABean ffab = new FFABean();
		String seriesNo = request.getParameter("seriesNo");
		ffab.setType(request.getParameter("type"));
		String error = "";
		ResultSet rset = null;
		if (seriesNo != null) {
			try {
				long seriesNum = Long.parseLong(seriesNo);
				rset = vJdbc.getSeries(seriesNum);
				if (rset.next()) {
					ffab.setVersion(rset.getString("version_desc"));
					ffab.setSeriesStartDate(rset.getString("start_date"));
					ffab.setSeriesEndDate(rset.getString("end_date"));
					ffab.setSeason(rset.getString("season"));
					ffab.setSeriesDayOfWeek(rset.getString("day_of_week"));
					java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(
							"dd/MM/yyyy");
					ffab.setSeriesCreatedDate(sdf.format(rset
							.getDate("created_date")));
					ffab
							.setSeriesNoRotations(rset
									.getString("no_of_rotations"));
					ffab.setSeriesNoFlights(rset.getString("no_of_flights"));
					ffab.setSeriesGateways(rset.getString("all_gateway"));
					ffab.setSeriesFrequency(rset.getString("frequency_desc"));
				} else {
					throw new FCException("Series not found", new Exception());
				}
				rset.getStatement().close();
				rset.close();

				rset = vJdbc.getSeriesDetail(seriesNum);
				java.util.Collection allKeys = new java.util.Vector(); // Vector
				// will
				// maintain
				// insert
				// order
				java.util.Map rotationSectors = new java.util.TreeMap(); // Treemap's
				// keySet
				// will
				// not
				// and we need to display in retrieval order
				while (rset.next()) {
					if (!"C".equals(rset.getString("record_status"))) {
						java.util.Collection sectors;
						String key = rset.getString("one_column");
						if (rotationSectors.containsKey(key)) {
							sectors = (Vector) rotationSectors.get(key);
						} else {
							sectors = new Vector();
							rotationSectors.put(key, sectors);
							allKeys.add(key);
						}
						sectors
								.add(ffab.new Sector(
										FCUtils
												.notNull(rset
														.getString("gateway_code_from")),
										FCUtils.notNull(rset
												.getString("gateway_code_to")),
										FCUtils.notNull(rset
												.getString("departure_time")),
										FCUtils.notNull(rset
												.getString("arrival_time")),
										FCUtils.notNull(rset
												.getString("flight_code")),
										FCUtils.notNull(rset
												.getString("flight_number")),
										FCUtils.notNull(rset
												.getString("carrier")),
										FCUtils.notNull(rset
												.getString("aircraft_type")),
										FCUtils.notNull(rset
												.getString("day_of_week")),
										FCUtils
												.parseDate(rset
														.getString("min_departure_date")),
										FCUtils
												.parseDate(rset
														.getString("max_departure_date")),
										rset.getString("ffa_key")));
					}
				}
				ffab.setRotationSectors(rotationSectors);
				ffab.setAllKeys(allKeys);
				rset.getStatement().close();
				rset.close();

				Map sectorDates = new Hashtable();
				for (Iterator i = rotationSectors.keySet().iterator(); i
						.hasNext();) {
					String sectorKey = (String) i.next();
					FFABean.Sector sector = (FFABean.Sector) ((Vector) (rotationSectors
							.get(sectorKey))).elementAt(0);
					String key = sector.getKey();
					Collection rotationDates = new TreeSet();
					rset = vJdbc.getRotationDepDates(seriesNum,
							sector.getKey(), sector.getMinDepartureDate(),
							sector.getMaxDepartureDate());
					while (rset.next()) {
						rotationDates.add(rset.getDate("departure_date"));
					}
					rset.getStatement().close();
					rset.close();
					sectorDates.put(sectorKey, rotationDates);
				}
				ffab.setSectorDates(sectorDates);

				Collection cancelledSectors = new TreeSet();
				rset = vJdbc.getCancelledSeries(seriesNum);
				while (rset.next()) {
					java.sql.Date cd = rset.getDate("departure_date");
					if (!cancelledSectors.contains(cd)) {
						cancelledSectors.add(cd);
					}
				}
				ffab.setCancelledSectors(cancelledSectors);
				rset.getStatement().close();
				rset.close();
				//System.out.println("Type: "+ffab.getType());
				rset = vJdbc.getAllotmentsByDepDate(seriesNum, "S".equals(ffab
						.getType()));
				TreeMap dateMap = new TreeMap();
				TreeMap sectorMap;
				Hashtable sharerMap;
				java.util.Collection allSharers = new java.util.TreeSet();
				int sectorCnt = 0;
				while (rset.next()) {
					java.sql.Date date = rset.getDate("departure_date");
					String sector = sectorCnt + ";"
							+ rset.getString("gateway_code_from") + "-"
							+ rset.getString("gateway_code_to");
					String sharer = rset.getString("customer_code") + ";"
							+ rset.getString("seat_class");
					if (!allSharers.contains(sharer)) {
						allSharers.add(sharer);
					}
					if (dateMap.containsKey(date)) {
						sectorMap = (TreeMap) dateMap.get(date);
					} else {
						sectorCnt = 0;
						sectorMap = new TreeMap();
						dateMap.put(date, sectorMap);
					}
					if (sectorMap.containsKey(sector)) {
						sharerMap = (Hashtable) sectorMap.get(sector);
					} else {
						sharerMap = new Hashtable();
						sector = ++sectorCnt + ";"
								+ rset.getString("gateway_code_from") + "-"
								+ rset.getString("gateway_code_to");
						sectorMap.put(sector, sharerMap);
					}
					int numSeats = rset.getInt("no_of_seat");
					if (sharerMap.containsKey(sharer)) {
						numSeats += ((Integer) sharerMap.get(sharer))
								.intValue();
					}
					sharerMap.put(sharer, new Integer(numSeats));
				}
				ffab.setAllSharers(allSharers);
				ffab.setSharerAllocations(dateMap);
				rset.getStatement().close();
				rset.close();

				rset = vJdbc.getAmendmentDetails(seriesNo);
				java.util.Collection commentsHolder = new java.util.Vector();
				while (rset.next()) {
					commentsHolder.add(new CommentHolderBean(FCUtils
							.dateToString(rset.getDate("AMENDED_DATE")), rset
							.getString("AMENDED_USER_ID"), rset
							.getString("DESCRIPTION"), rset
							.getString("VK_COMMENTS")));
				}
				request.setAttribute("seriescomments", commentsHolder);
				rset.getStatement().close();
				rset.close();
			} catch (NumberFormatException e) {
				error = "Invalid series number (" + seriesNo + ")";
			} catch (FCException e) {
				e.printStackTrace();
				error = e.getMessage();
			}
		}
		vJdbc.closeResultSet(rset);
		vJdbc.close();
		request.setAttribute("ffabean", ffab);
		return (mapping.findForward(ForwardConstants.SUCCESS));
	}

}