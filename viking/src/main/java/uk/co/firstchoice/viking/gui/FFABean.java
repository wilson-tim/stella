/*
 * FFABean.java
 *
 * Created on 16 January 2004, 11:30
 */

package uk.co.firstchoice.viking.gui;

//import uk.co.firstchoice.fcutil.*;

/**
 * 
 * @author Lars Svensson
 */
public class FFABean {

	/* Header information */
	private String version;

	private String seriesStartDate;

	private String seriesEndDate;

	private String season;

	private String seriesDayOfWeek;

	private String seriesCreatedDate;

	private String seriesNoRotations;

	private String seriesNoFlights;

	private String seriesGateways;

	private String seriesFrequency;

	private String type;

	/*  */
	private java.util.Collection allKeys;

	private java.util.Map rotationSectors;

	private java.util.Map sectorDates;

	private java.util.Collection cancelledSectors;

	private java.util.Collection allSharers;

	private java.util.TreeMap sharerAllocations;

	/** Creates a new empty instance of AllocationBean */
	public FFABean() {
	}

	/**
	 * Getter for property season.
	 * 
	 * @return Value of property season.
	 *  
	 */
	public java.lang.String getSeason() {
		return season;
	}

	/**
	 * Setter for property season.
	 * 
	 * @param season
	 *            New value of property season.
	 *  
	 */
	public void setSeason(java.lang.String season) {
		this.season = season;
	}

	/**
	 * Getter for property seriesCreatedDate.
	 * 
	 * @return Value of property seriesCreatedDate.
	 *  
	 */
	public java.lang.String getSeriesCreatedDate() {
		return seriesCreatedDate;
	}

	/**
	 * Setter for property seriesCreatedDate.
	 * 
	 * @param seriesCreatedDate
	 *            New value of property seriesCreatedDate.
	 *  
	 */
	public void setSeriesCreatedDate(java.lang.String seriesCreatedDate) {
		this.seriesCreatedDate = seriesCreatedDate;
	}

	/**
	 * Getter for property seriesDayOfWeek.
	 * 
	 * @return Value of property seriesDayOfWeek.
	 *  
	 */
	public java.lang.String getSeriesDayOfWeek() {
		return seriesDayOfWeek;
	}

	/**
	 * Setter for property seriesDayOfWeek.
	 * 
	 * @param seriesDayOfWeek
	 *            New value of property seriesDayOfWeek.
	 *  
	 */
	public void setSeriesDayOfWeek(java.lang.String seriesDayOfWeek) {
		this.seriesDayOfWeek = seriesDayOfWeek;
	}

	/**
	 * Getter for property seriesEndDate.
	 * 
	 * @return Value of property seriesEndDate.
	 *  
	 */
	public java.lang.String getSeriesEndDate() {
		return seriesEndDate;
	}

	/**
	 * Setter for property seriesEndDate.
	 * 
	 * @param seriesEndDate
	 *            New value of property seriesEndDate.
	 *  
	 */
	public void setSeriesEndDate(java.lang.String seriesEndDate) {
		this.seriesEndDate = seriesEndDate;
	}

	/**
	 * Getter for property seriesFrequency.
	 * 
	 * @return Value of property seriesFrequency.
	 *  
	 */
	public java.lang.String getSeriesFrequency() {
		return seriesFrequency;
	}

	/**
	 * Setter for property seriesFrequency.
	 * 
	 * @param seriesFrequency
	 *            New value of property seriesFrequency.
	 *  
	 */
	public void setSeriesFrequency(java.lang.String seriesFrequency) {
		this.seriesFrequency = seriesFrequency;
	}

	/**
	 * Getter for property seriesGateways.
	 * 
	 * @return Value of property seriesGateways.
	 *  
	 */
	public java.lang.String getSeriesGateways() {
		return seriesGateways;
	}

	/**
	 * Setter for property seriesGateways.
	 * 
	 * @param seriesGateways
	 *            New value of property seriesGateways.
	 *  
	 */
	public void setSeriesGateways(java.lang.String seriesGateways) {
		this.seriesGateways = seriesGateways;
	}

	/**
	 * Getter for property seriesNoFlights.
	 * 
	 * @return Value of property seriesNoFlights.
	 *  
	 */
	public java.lang.String getSeriesNoFlights() {
		return seriesNoFlights;
	}

	/**
	 * Setter for property seriesNoFlights.
	 * 
	 * @param seriesNoFlights
	 *            New value of property seriesNoFlights.
	 *  
	 */
	public void setSeriesNoFlights(java.lang.String seriesNoFlights) {
		this.seriesNoFlights = seriesNoFlights;
	}

	/**
	 * Getter for property seriesNoRotations.
	 * 
	 * @return Value of property seriesNoRotations.
	 *  
	 */
	public java.lang.String getSeriesNoRotations() {
		return seriesNoRotations;
	}

	/**
	 * Setter for property seriesNoRotations.
	 * 
	 * @param seriesNoRotations
	 *            New value of property seriesNoRotations.
	 *  
	 */
	public void setSeriesNoRotations(java.lang.String seriesNoRotations) {
		this.seriesNoRotations = seriesNoRotations;
	}

	/**
	 * Getter for property seriesStartDate.
	 * 
	 * @return Value of property seriesStartDate.
	 *  
	 */
	public java.lang.String getSeriesStartDate() {
		return seriesStartDate;
	}

	/**
	 * Setter for property seriesStartDate.
	 * 
	 * @param seriesStartDate
	 *            New value of property seriesStartDate.
	 *  
	 */
	public void setSeriesStartDate(java.lang.String seriesStartDate) {
		this.seriesStartDate = seriesStartDate;
	}

	/**
	 * Getter for property version.
	 * 
	 * @return Value of property version.
	 *  
	 */
	public java.lang.String getVersion() {
		return version;
	}

	/**
	 * Setter for property version.
	 * 
	 * @param version
	 *            New value of property version.
	 *  
	 */
	public void setVersion(java.lang.String version) {
		this.version = version;
	}

	/**
	 * Getter for property sharerAllocations.
	 * 
	 * @return Value of property sharerAllocations.
	 *  
	 */
	public java.util.TreeMap getSharerAllocations() {
		return sharerAllocations;
	}

	/**
	 * Setter for property sharerAllocations.
	 * 
	 * @param sharerAllocations
	 *            New value of property sharerAllocations.
	 *  
	 */
	public void setSharerAllocations(java.util.TreeMap sharerAllocations) {
		this.sharerAllocations = sharerAllocations;
	}

	/**
	 * Getter for property allSharers.
	 * 
	 * @return Value of property allSharers.
	 *  
	 */
	public java.util.Collection getAllSharers() {
		return allSharers;
	}

	/**
	 * Setter for property allSharers.
	 * 
	 * @param allSharers
	 *            New value of property allSharers.
	 *  
	 */
	public void setAllSharers(java.util.Collection allSharers) {
		this.allSharers = allSharers;
	}

	/**
	 * Getter for property cancelledSectors.
	 * 
	 * @return Value of property cancelledSectors.
	 *  
	 */
	public java.util.Collection getCancelledSectors() {
		return cancelledSectors;
	}

	/**
	 * Setter for property cancelledSectors.
	 * 
	 * @param cancelledSectors
	 *            New value of property cancelledSectors.
	 *  
	 */
	public void setCancelledSectors(java.util.Collection cancelledSectors) {
		this.cancelledSectors = cancelledSectors;
	}

	/**
	 * Getter for property rotationSectors.
	 * 
	 * @return Value of property rotationSectors.
	 *  
	 */
	public java.util.Map getRotationSectors() {
		return rotationSectors;
	}

	/**
	 * Setter for property rotationSectors.
	 * 
	 * @param allotments
	 *            New value of property rotationSectors.
	 *  
	 */
	public void setRotationSectors(java.util.Map rotationSectors) {
		this.rotationSectors = rotationSectors;
	}

	/**
	 * Getter for property sectorDates.
	 * 
	 * @return Value of property sectorDates.
	 *  
	 */
	public java.util.Map getSectorDates() {
		return sectorDates;
	}

	/**
	 * Setter for property sectorDates.
	 * 
	 * @param sectorDates
	 *            New value of property sectorDates.
	 *  
	 */
	public void setSectorDates(java.util.Map sectorDates) {
		this.sectorDates = sectorDates;
	}

	/**
	 * Getter for property allKeys.
	 * 
	 * @return Value of property allKeys.
	 *  
	 */
	public java.util.Collection getAllKeys() {
		return allKeys;
	}

	/**
	 * Setter for property allKeys.
	 * 
	 * @param allKeys
	 *            New value of property allKeys.
	 *  
	 */
	public void setAllKeys(java.util.Collection allKeys) {
		this.allKeys = allKeys;
	}

	/**
	 * Getter for property type.
	 * 
	 * @return Value of property type.
	 *  
	 */
	public java.lang.String getType() {
		return type;
	}

	/**
	 * Setter for property type.
	 * 
	 * @param type
	 *            New value of property type.
	 *  
	 */
	public void setType(java.lang.String type) {
		this.type = type;
	}

	public class Sector {

		private String gwFrom;

		private String gwTo;

		private String depTime;

		private String arrTime;

		private String flightCode;

		private String flightNumber;

		private String carrier;

		private String aircraftType;

		private String dow;

		private java.sql.Date minDepartureDate;

		private java.sql.Date maxDepartureDate;

		private String key;

		Sector(String gwFrom, String gwTo, String depTime, String arrTime,
				String flightCode, String flightNumber, String carrier,
				String aircraftType, String dow,
				java.sql.Date minDepartureDate, java.sql.Date maxDepartureDate,
				String key) {
			this.gwFrom = gwFrom;
			this.gwTo = gwTo;
			this.depTime = depTime;
			this.arrTime = arrTime;
			this.flightCode = flightCode;
			this.flightNumber = flightNumber;
			this.carrier = carrier;
			this.aircraftType = aircraftType;
			this.dow = dow;
			this.minDepartureDate = minDepartureDate;
			this.maxDepartureDate = maxDepartureDate;
			this.key = key;
		}

		/**
		 * Getter for property aircraftType.
		 * 
		 * @return Value of property aircraftType.
		 *  
		 */
		public java.lang.String getAircraftType() {
			return aircraftType;
		}

		/**
		 * Getter for property arrTime.
		 * 
		 * @return Value of property arrTime.
		 *  
		 */
		public java.lang.String getArrTime() {
			return arrTime;
		}

		/**
		 * Getter for property depTime.
		 * 
		 * @return Value of property depTime.
		 *  
		 */
		public java.lang.String getDepTime() {
			return depTime;
		}

		/**
		 * Getter for property flightCode.
		 * 
		 * @return Value of property flightCode.
		 *  
		 */
		public java.lang.String getFlightCode() {
			return flightCode;
		}

		/**
		 * Getter for property flightNumber.
		 * 
		 * @return Value of property flightNumber.
		 *  
		 */
		public java.lang.String getFlightNumber() {
			return flightNumber;
		}

		/**
		 * Getter for property carrier.
		 * 
		 * @return Value of property carrier.
		 *  
		 */
		public java.lang.String getCarrier() {
			return carrier;
		}

		/**
		 * Getter for property gwFrom.
		 * 
		 * @return Value of property gwFrom.
		 *  
		 */
		public java.lang.String getGwFrom() {
			return gwFrom;
		}

		/**
		 * Getter for property gwTo.
		 * 
		 * @return Value of property gwTo.
		 *  
		 */
		public java.lang.String getGwTo() {
			return gwTo;
		}

		/**
		 * Getter for property dow.
		 * 
		 * @return Value of property dow.
		 *  
		 */
		public java.lang.String getDow() {
			return dow;
		}

		/**
		 * Getter for property key.
		 * 
		 * @return Value of property key.
		 *  
		 */
		public java.lang.String getKey() {
			return key;
		}

		/**
		 * Getter for property maxDepartureDate.
		 * 
		 * @return Value of property maxDepartureDate.
		 */
		public java.sql.Date getMaxDepartureDate() {
			return maxDepartureDate;
		}

		/**
		 * Setter for property maxDepartureDate.
		 * 
		 * @param maxDepartureDate
		 *            New value of property maxDepartureDate.
		 */
		public void setMaxDepartureDate(java.sql.Date maxDepartureDate) {
			this.maxDepartureDate = maxDepartureDate;
		}

		/**
		 * Getter for property minDepartureDate.
		 * 
		 * @return Value of property minDepartureDate.
		 */
		public java.sql.Date getMinDepartureDate() {
			return minDepartureDate;
		}

		/**
		 * Setter for property minDepartureDate.
		 * 
		 * @param minDepartureDate
		 *            New value of property minDepartureDate.
		 */
		public void setMinDepartureDate(java.sql.Date minDepartureDate) {
			this.minDepartureDate = minDepartureDate;
		}

	}

}