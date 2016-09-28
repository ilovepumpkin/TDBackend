package testdash.rest.resource;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.restlet.Context;
import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.data.Status;
import org.restlet.resource.DomRepresentation;
import org.restlet.resource.Representation;
import org.restlet.resource.Resource;
import org.restlet.resource.StringRepresentation;
import org.restlet.resource.Variant;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import testdash.exception.TestDashException;

public class SCurveResource extends BaseResource {
	private String filePath;
	protected String scurveData;
	private String type;

	public SCurveResource(Context context, Request request, Response response) {
		super(context, request, response);

		getVariants().add(new Variant(MediaType.TEXT_XML));
		/*
		 * Get the scurve file location according to the requested scurve type.
		 */
		type = (String) getRequest().getAttributes().get("type");
		if (type.equalsIgnoreCase("er")) {
			filePath = getTeamContext().getErSCurveFilePath();
		} else if (type.equalsIgnoreCase("defect")) {
			filePath = getTeamContext().getDefectSCurveFilePath();
		}

		createEmptySCurveFile(filePath, type);

		setModifiable(true);
	}

	private void createEmptySCurveFile(String filePath, String type) {
		File file = new File(filePath);
		if (!file.exists()) {
			try {
				file.createNewFile();
				FileWriter fstream = new FileWriter(filePath);
				BufferedWriter out = new BufferedWriter(fstream);
				String fileContent = "";
				if (type.equalsIgnoreCase("er")) {
					fileContent = "Timeline,Planned Attempts,Planned Complete,Actual Attempts,Actual Complete\n";
				} else if (type.equalsIgnoreCase("defect")) {
					fileContent = "Timeline,Projected Defects,Projected Valid,Actual Defects,Actual Valid\n";
				}
				out.write(fileContent);
				out.close();
			} catch (IOException e) {
				throw new TestDashException("Fail to create empty SCurve file "
						+ filePath, e);
			}
		}
	}

	@Override
	public boolean allowPost() {
		return true;
	}

	private String convertXmlToCSV(String xmlString, String titleLine) {
		DocumentBuilderFactory docFactory = DocumentBuilderFactory
				.newInstance();
		DocumentBuilder docBuilder;
		try {
			docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.parse(new InputSource(new StringReader(
					xmlString)));
			Element rootElem = doc.getDocumentElement();

			StringBuffer sb = new StringBuffer(titleLine);
			sb.append("\n");

			NodeList rowNodeList = rootElem.getChildNodes();
			int nodeListLength = rowNodeList.getLength();
			for (int i = 0; i < nodeListLength; i++) {
				Node rowNode = rowNodeList.item(i);
				if (rowNode.getNodeType() == Node.ELEMENT_NODE) {
					NodeList colNodeList = rowNode.getChildNodes();
					// if (i == 0) {
					// for (int j = 0; j < colNodeList.getLength(); j++) {
					// Node colName = colNodeList.item(j);
					// if (colName.getNodeType() == Node.ELEMENT_NODE) {
					// if (j != 0)
					// sb.append(",");
					// sb.append(colName.getNodeName());
					// }
					// }
					// sb.append("\n");
					// }
					for (int j = 0; j < colNodeList.getLength(); j++) {
						Node valueNode = colNodeList.item(j);
						if (valueNode.getNodeType() == Node.ELEMENT_NODE) {
							if (j != 0)
								sb.append(",");
							sb.append(valueNode.getTextContent());
						}

					}
					sb.append("\n");
				}
			}

			return sb.toString();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public void acceptRepresentation(Representation entity) {
		try {
			String titleLine = null;
			if (type.equalsIgnoreCase("er")) {
				titleLine = "Timeline,Planned Attempts,Planned Complete,Actual Attempts,Actual Complete";
			} else if (type.equalsIgnoreCase("defect")) {
				titleLine = "Timeline,Projected Defects,Projected Valid,Actual Defects,Actual Valid";
			}

			String csvContent = convertXmlToCSV(entity.getText(), titleLine);
			csvContent = csvContent.replaceAll("_", " ");
			FileWriter fstream = new FileWriter(filePath);
			BufferedWriter out = new BufferedWriter(fstream);
			out.write(csvContent);
			out.close();

		} catch (IOException e) {
			throw new TestDashException(
					"Fail to persist the updated SCurve data.", e);
		}

		getResponse().setStatus(Status.SUCCESS_CREATED);
		Representation rep = getRepresentation(new Variant(MediaType.TEXT_XML));
		// Indicates where is located the new resource.
		rep.setIdentifier(getRequest().getResourceRef().getIdentifier());
		getResponse().setEntity(rep);

	}

	public Representation getRepresentation(Variant variant) {
		// if (scurveData != null) {
		// try {
		//
		// String titleLine = null;
		// if (type.equalsIgnoreCase("er")) {
		// titleLine =
		// "Timeline,Planned Attempts,Planned Complete,Actual Attempts,Actual Complete"
		// ;
		// } else if (type.equalsIgnoreCase("defect")) {
		// titleLine =
		// "Timeline,Projected Defects,Projected Valid,Actual Defects,Actual Valid"
		// ;
		// }
		//
		// String csvContent = convertXmlToCSV(scurveData, titleLine);
		// csvContent = csvContent.replaceAll("_", " ");
		// FileWriter fstream = new FileWriter(filePath);
		// BufferedWriter out = new BufferedWriter(fstream);
		// out.write(csvContent);
		// out.close();
		//
		// } catch (IOException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// }

		DomRepresentation representation = null;
		try {
			BufferedReader br = new BufferedReader(new FileReader(filePath));
			String line = null;
			List<String> colList = new ArrayList<String>();
			representation = new DomRepresentation(MediaType.TEXT_XML);
			Document d = representation.getDocument();
			// Element root=d.createElement("SCurve");
			// d.appendChild(root);
			// Element typeElem=d.createElement("Type");
			// typeElem.appendChild(d.createTextNode(this.type));
			// root.appendChild(typeElem);
			Element rowSet = d.createElement("RowSet");
			d.appendChild(rowSet);

			for (int j = 0; j <= 5; j++) {
				colList.add("Col" + String.valueOf(j));
			}

			// Skip the title line
			br.readLine();
			while ((line = br.readLine()) != null) {
				// if (colList.size() == 0) {
				// line = line.replace(" ", "_");
				// StringTokenizer st = new StringTokenizer(line, ",");
				// while (st.hasMoreTokens()) {
				// String colName = st.nextToken();
				// colList.add(colName);
				// }
				// } else {
				Element row = d.createElement("Row");
				StringTokenizer st = new StringTokenizer(line, ",");
				int i = 0;
				while (st.hasMoreTokens()) {
					String value = st.nextToken();
					String colName = colList.get(i++).toString();
					Element eltName = d.createElement(colName);
					eltName.appendChild(d.createTextNode(value));
					row.appendChild(eltName);
				}
				rowSet.appendChild(row);
				// }

			}
			d.normalizeDocument();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return representation;
	}
}
