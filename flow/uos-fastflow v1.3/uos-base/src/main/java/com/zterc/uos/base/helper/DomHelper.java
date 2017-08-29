package com.zterc.uos.base.helper;

import java.io.ByteArrayOutputStream;
import java.io.Reader;
import java.io.StringReader;
import java.util.List;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentFactory;
import org.dom4j.DocumentHelper;
import org.dom4j.DocumentType;
import org.dom4j.Element;
import org.dom4j.QName;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

@SuppressWarnings("all")
public class DomHelper {
	/** 缺省字符集 */
	public static final String DEFAULT_ENCODING = "GBK";

	/**
	 * 在某元素前插入一个元素
	 * 
	 * @param parent
	 *            Element
	 * @param name
	 *            Element
	 * @param ref
	 *            Element
	 * @throws RuntimeException
	 * @return Element
	 */
	public static Element insertBefore(Element parent, String name, Element ref) {
		Element newElement = DocumentHelper.createElement(new QName(name,
				parent.getNamespace()));
		List elements = ref.getParent().elements();
		int refIndex = elements.indexOf(ref);
		elements.add(refIndex, newElement);
		return (Element) elements.get(refIndex);
	}

	/**
	 * 在某元素后插入一个元素
	 * 
	 * @param parent
	 *            Element
	 * @param name
	 *            Element
	 * @param ref
	 *            Element
	 * @throws RuntimeException
	 * @return Element
	 */
	public static Element insertNext(Element parent, String name, Element ref) {
		Element newElement = DocumentHelper.createElement(new QName(name,
				parent.getNamespace()));
		List elements = ref.getParent().elements();
		int refIndex = elements.indexOf(ref) + 1;
		elements.add(refIndex, newElement);
		return (Element) elements.get(refIndex);
	}

	//

	/**
	 * Return the child element with the given name. The element must be in the
	 * same name space as the parent element.
	 * 
	 * @param element
	 *            The parent element
	 * @param name
	 *            The child element name
	 * @return The child element
	 */
	public static Element child(Element element, String name) {
		return element.element(new QName(name, element.getNamespace()));
	}

	/**
	 * 得到给定结点下的孩子节点
	 * 
	 * @param element
	 *            节点
	 * @param name
	 *            子节点名称
	 * @param optional
	 *            是否是可选的
	 * @return 子节点
	 * @throws RuntimeException
	 */
	public static Element child(Element element, String name, boolean optional) {
		Element child = element
				.element(new QName(name, element.getNamespace()));
		return child;
	}

	/**
	 * Return the child elements with the given name. The elements must be in
	 * the same name space as the parent element.
	 * 
	 * @param element
	 *            The parent element
	 * @param name
	 *            The child element name
	 * @return The child elements
	 */
	public static List<Element> children(Element element, String name) {
		return (List<Element>)element.elements(new QName(name, element.getNamespace()));
	}

	/**
	 * 得到某个节点下的属性信息
	 * 
	 * @param element
	 *            节点
	 * @param name
	 *            属性名
	 * @param optional
	 *            是否是可选的
	 * @return 值
	 * @throws RuntimeException
	 */
	public static String getAttribute(Element element, String name,
			boolean optional) {
		Attribute attr = element.attribute(name);
		if (attr != null) {
			return attr.getValue();
		} else {
			return null;
		}
	}

	/**
	 * 得到某个节点下的属性信息，值以字符串的形式返回
	 * 
	 * @param element
	 *            节点
	 * @param name
	 *            属性名
	 * @param optional
	 *            是否是可选的
	 * @return 值
	 * @throws RuntimeException
	 */
	public static String getAttributeAsString(Element element, String name,
			boolean optional) {
		return getAttribute(element, name, optional);
	}

	/**
	 * 得到某个节点下的属性信息，值以整数的形式返回。 如果没有值或是转化为整形，那么抛出异常。
	 * 
	 * @param element
	 *            节点
	 * @param name
	 *            属性名
	 * @param optional
	 *            是否是可选的
	 * @return 值
	 * @throws RuntimeException
	 */
	public static int getAttributeAsInt(Element element, String name,
			boolean optional) {
		return Integer.parseInt(getAttribute(element, name, optional));
	}

	/**
	 * 得到某个节点下的属性信息，值以整数的形式返回。 如果该值是可选的，并且没有该值的话，就返回调用者提供缺省值。
	 * 
	 * @param element
	 *            节点
	 * @param name
	 *            属性名
	 * @param defaultValue
	 *            缺省值
	 * @param optional
	 *            是否是可选的
	 * @return 值
	 * @throws RuntimeException
	 */
	public static int getAttributeAsInt(Element element, String name,
			int defaultValue, boolean optional) {
		String value = getAttribute(element, name, optional);
		if ((optional) && ((value == null) || (value.equals("")))) {
			return defaultValue;
		} else {
			return Integer.parseInt(value);
		}
	}

	/**
	 * 得到某个节点下的属性信息，值以float的形式返回。 如果没有值或是转化为float，那么抛出异常。
	 * 
	 * @param element
	 *            节点
	 * @param name
	 *            属性名
	 * @param optional
	 *            是否是可选的
	 * @return 值
	 * @throws RuntimeException
	 */
	public static float getAttributeAsFloat(Element element, String name,
			boolean optional) {
		return Float.parseFloat(getAttribute(element, name, optional));
	}

	/**
	 * 得到某个节点下的属性信息，值以float的形式返回。 如果没有值,返回缺省值；如果有，那么转化为float，如果不能转化那么抛出异常。
	 * 
	 * @param element
	 *            节点
	 * @param name
	 *            属性名
	 * @param defaultValue
	 *            缺省值
	 * @param optional
	 *            是否是可选的
	 * @return 值
	 * @throws RuntimeException
	 */
	public static float getAttributeAsFloat(Element element, String name,
			float defaultValue, boolean optional) {
		String value = getAttribute(element, name, optional);
		if ((optional) && ((value == null) || (value.equals("")))) {
			return defaultValue;
		} else {
			return Float.parseFloat(value);
		}
	}

	/**
	 * 得到某个节点下的属性信息，值以长整数的形式返回。 如果没有值或是转化为整形，那么抛出异常。
	 * 
	 * @param element
	 *            节点
	 * @param name
	 *            属性名
	 * @param optional
	 *            是否是可选的
	 * @return 值
	 * @throws RuntimeException
	 */
	public static long getAttributeAsLong(Element element, String name,
			boolean optional) {
		return Long.parseLong(getAttribute(element, name, optional));
	}

	/**
	 * 得到某个节点下的属性信息，值以整数的形式返回。 如果该值是可选的，并且没有该值的话，就返回调用者提供缺省值。
	 * 
	 * @param element
	 *            节点
	 * @param name
	 *            属性名
	 * @param defaultValue
	 *            缺省值
	 * @param optional
	 *            是否是可选的
	 * @return 值
	 * @throws RuntimeException
	 */
	public static long getAttributeAsLong(Element element, String name,
			long defaultValue, boolean optional) {
		String value = getAttribute(element, name, optional);
		if ((optional) && ((value == null) || (value.equals("")))) {
			return defaultValue;
		} else {
			return Long.parseLong(value);
		}
	}

	/**
	 * 得到某个节点下的某名字的第一个孩子节点
	 * 
	 * @param element
	 *            节点
	 * @param name
	 *            子节点名称
	 * @param optional
	 *            是否是可选的
	 * @return 值
	 * @throws RuntimeException
	 */
	public static Element getFirstChild(Element element, String name,
			boolean optional) {
		List list = element.elements(new QName(name, element.getNamespace()));
		// 如果数目大于0，那么直接取第一个就可以了
		if (list.size() > 0) {
			return (Element) list.get(0);
		} else {
			return null;
		}
	}

	/**
	 * 得到同名兄弟节点,同名的第一个节点，可以是自己
	 * 
	 * @param element
	 *            节点
	 * @param optional
	 *            是否是可选的
	 * @return 节点
	 * @throws RuntimeException
	 */
	public static Element getSibling(Element element, boolean optional) {
		return getSibling(element, element.getName(), optional);
	}

	/**
	 * 按名称得到兄弟节点
	 * 
	 * @param element
	 *            节点
	 * @param name
	 *            子节点名称
	 * @param optional
	 *            是否是可选的
	 * @return 节点
	 * @throws RuntimeException
	 */
	public static Element getSibling(Element element, String name,
			boolean optional) {
		List list = element.getParent().elements(name);
		if (list.size() > 0) {
			return (Element) list.get(0);
		} else {
			if (!optional) {
				throw new RuntimeException(name + " element expected after "
						+ element.getName() + ".");
			} else {
				return null;
			}
		}
	}

	/**
	 * 得到给定节点的值,以字符串返回
	 * 
	 * @param element
	 *            节点
	 * @param optional
	 *            是否是可选的
	 * @return 值
	 * @throws RuntimeException
	 */
	public static String getContent(Element element, boolean optional) {
		String content = element.getText();
		if (content == null && !optional) {
			throw new RuntimeException(element.getName()
					+ " element: content expected.");
		} else {
			return content;
		}
	}

	/**
	 * 得到给定节点的值,以字符串返回
	 * 
	 * @param element
	 *            节点
	 * @param optional
	 *            是否是可选的
	 * @return 值
	 * @throws RuntimeException
	 */
	public static String getContentAsString(Element element, boolean optional) {
		return getContent(element, optional);
	}

	/**
	 * 得到给定节点的值,以整数类型返回
	 * 
	 * @param element
	 *            节点
	 * @param optional
	 *            是否是可选的
	 * @return 值
	 * @throws RuntimeException
	 */
	public static int getContentAsInt(Element element, boolean optional) {
		try {
			return Integer.parseInt(getContent(element, optional));
		} catch (NumberFormatException exception) {
			throw new RuntimeException(element.getName()
					+ " element: content format error.", exception);
		}
	}

	/**
	 * 得到给定节点的值,以整数类型返回
	 * 
	 * @param element
	 *            节点
	 * @param defaultValue
	 *            缺省值
	 * @param optional
	 *            是否是可选的
	 * @return 值
	 * @throws RuntimeException
	 */
	public static int getContentAsInt(Element element, int defaultValue,
			boolean optional) {
		String value = getContent(element, optional);
		if ((optional) && (value == null || value.equals(""))) {
			return defaultValue;
		} else {
			try {
				return Integer.parseInt(value);
			} catch (NumberFormatException exception) {
				throw new RuntimeException(element.getName()
						+ " element: content format error.", exception);
			}
		}
	}

	/**
	 * 得到给定节点的值,以长整类型返回
	 * 
	 * @param element
	 *            节点
	 * @param optional
	 *            是否是可选的
	 * @return 值
	 * @throws RuntimeException
	 */
	public static long getContentAsLong(Element element, boolean optional) {
		try {
			return Long.parseLong(getContent(element, optional));
		} catch (NumberFormatException exception) {
			throw new RuntimeException(element.getName()
					+ " element: content format error.", exception);
		}
	}

	/**
	 * 得到给定节点的值,以整数类型返回
	 * 
	 * @param element
	 *            节点
	 * @param defaultValue
	 *            缺省值
	 * @param optional
	 *            是否是可选的
	 * @return 值
	 * @throws RuntimeException
	 */
	public static long getContentAsLong(Element element, long defaultValue,
			boolean optional) {
		String value = getContent(element, optional);
		if ((optional) && (value == null || value.equals(""))) {
			return defaultValue;
		} else {
			try {
				return Long.parseLong(value);
			} catch (NumberFormatException exception) {
				throw new RuntimeException(element.getName()
						+ " element: content format error.", exception);
			}
		}
	}

	/**
	 * 得到给定节点的值,以浮点类型返回
	 * 
	 * @param element
	 *            节点
	 * @param optional
	 *            是否是可选的
	 * @return 值
	 * @throws RuntimeException
	 */
	public static float getContentAsFloat(Element element, boolean optional) {
		try {
			return Float.parseFloat(getContent(element, optional));
		} catch (NumberFormatException exception) {
			throw new RuntimeException(element.getName()
					+ " element: content format error.", exception);
		}
	}

	/**
	 * 得到给定节点的值,以浮点类型返回
	 * 
	 * @param element
	 *            节点
	 * @param defaultValue
	 *            缺省值
	 * @param optional
	 *            是否是可选的
	 * @return 值
	 * @throws RuntimeException
	 */
	public static float getContentAsFloat(Element element, float defaultValue,
			boolean optional) {
		String value = getContent(element, optional);
		if ((optional) && (value == null || value.equals(""))) {
			return defaultValue;
		} else {
			try {
				return Float.parseFloat(value);
			} catch (NumberFormatException exception) {
				throw new RuntimeException(element.getName()
						+ " element: content format error.", exception);
			}
		}
	}

	/**
	 * 给定父节点和子节点名称，得到子节点值
	 * 
	 * @param root
	 *            父节点
	 * @param subTagName
	 *            子节点
	 * @return 值
	 */
	public static String getSubTagValue(Element root, String subTagName) {
		String returnString = root.elementText(subTagName);
		return returnString;
	}

	/**
	 * 给定父节点，子节点名称，孙节点名称；得到值
	 * 
	 * @param root
	 *            父节点
	 * @param tagName
	 *            子节点名称
	 * @param subTagName
	 *            孙节点名称
	 * @return 值
	 */
	public static String getSubTagValue(Element root, String tagName,
			String subTagName) {
		Element child = root.element(tagName);
		String returnString = child.elementText(subTagName);
		return returnString;
	}

	/**
	 * 新Element节点，值为String类型
	 * 
	 * @param parent
	 *            父节点
	 * @param name
	 *            新节点名称
	 * @param value
	 *            新节点值
	 * @return element
	 * @throws RuntimeException
	 */
	public static Element appendChild(Element parent, String name, String value) {
		Element element = parent.addElement(new QName(name, parent
				.getNamespace()));
		if (value != null) {
			element.addText(value);
		}
		return element;
	}

	/**
	 * 增加新Element节点，无值
	 * 
	 * @param parent
	 *            父节点
	 * @param name
	 *            新节点名称
	 * @return Element 新建节点
	 * @throws RuntimeException
	 */
	public static Element appendChild(Element parent, String name) {
		return parent.addElement(new QName(name, parent.getNamespace()));
	}

	/**
	 * 增加新Element节点，值为int类型
	 * 
	 * @param parent
	 *            父节点
	 * @param name
	 *            新节点名称
	 * @param value
	 *            新节点值
	 * @return element
	 * @throws RuntimeException
	 */
	public static Element appendChild(Element parent, String name, int value) {
		return appendChild(parent, name, String.valueOf(value));
	}

	/**
	 * 增加新Element节点，值为长整形
	 * 
	 * @param parent
	 *            父节点
	 * @param name
	 *            新节点名称
	 * @param value
	 *            新节点值
	 * @return element
	 * @throws RuntimeException
	 */
	public static Element appendChild(Element parent, String name, long value) {
		return appendChild(parent, name, String.valueOf(value));
	}

	/**
	 * 新加一个float值类型的节点，值为浮点型
	 * 
	 * @param parent
	 *            父节点
	 * @param name
	 *            新节点的名称
	 * @param value
	 *            新节点的值
	 * @return element
	 * @throws RuntimeException
	 */
	public static Element appendChild(Element parent, String name, float value) {
		return appendChild(parent, name, String.valueOf(value));
	}

	/**
	 * 检查文档dtd定义是否正确
	 * 
	 * @param document
	 *            文档节点
	 * @param dtdPublicId
	 *            dtd定义
	 * @return boolean 相同返回true,否则false
	 */
	public static boolean checkDocumentType(Document document,
			String dtdPublicId) {
		DocumentType documentType = document.getDocType();
		if (documentType != null) {
			String publicId = documentType.getPublicID();
			return publicId != null && publicId.equals(dtdPublicId);
		}
		return true;
	}

	/**
	 * 新建文档
	 * 
	 * @return Document 文档节点
	 * @throws RuntimeException
	 */
	public static Document createDocument() {
		DocumentFactory factory = new DocumentFactory();
		Document document = factory.createDocument();
		return document;
	}

	/**
	 * 通过Reader读取Document文档 如果encodingStr为null或是""，那么采用缺省编码GB2312
	 * 
	 * @param in
	 *            Reader器
	 * @param encoding
	 *            编码器
	 * @return documment
	 * @throws RuntimeException
	 */
	public static Document fromXML(Reader in, String encoding) {
		try {
			if (encoding == null || encoding.equals("")) {
				encoding = DEFAULT_ENCODING;
			}
			SAXReader reader = new SAXReader();
			Document document = reader.read(in, encoding);
			return document;
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	/**
	 * 直接从字符串得到XML的Document
	 * 
	 * @param source
	 *            把一个字符串文本转化为XML的Document对象
	 * @param encoding
	 *            编码器
	 * @return <code>Document</code>
	 * @throws RuntimeException
	 */
	public static Document fromXML(String source, String encoding) {
		return fromXML(new StringReader(source), encoding);
	}

	/**
	 * 把XML的Document转化为java.io.Writer输出流 不支持给定Schema文件的校验
	 * 
	 * @param document
	 *            XML文档
	 * @param outWriter
	 *            输出写入器
	 * @param encoding
	 *            编码类型
	 * @throws RuntimeException
	 *             如果有任何异常转化为该异常输出
	 */
	public static void toXML(Document document, java.io.Writer outWriter,
			String encoding) {
		//
		OutputFormat outformat = OutputFormat.createPrettyPrint();
		if (encoding == null || encoding.trim().equals("")) {
			encoding = DEFAULT_ENCODING;
		}
		// 设置编码类型
		outformat.setEncoding(encoding);
		outformat.setTrimText(false);
		XMLWriter xmlWriter = null;
		try {
			xmlWriter = new XMLWriter(outWriter, outformat);
			xmlWriter.write(document);
			xmlWriter.flush();
		} catch (java.io.IOException ex) {
			throw new RuntimeException(ex);
		} finally {
			if (xmlWriter != null) {
				try {
					xmlWriter.close();
				} catch (java.io.IOException ex) {
				}
			}
		}
	}

	/**
	 * 把XML的Document转化为java.io.Writer输出流 不支持给定Schema文件的校验
	 * 
	 * @param document
	 *            XML文档
	 * @param outStream
	 *            输出写入器
	 * @param encoding
	 *            编码类型
	 * @throws RuntimeException
	 *             如果有任何异常转化为该异常输出
	 */
	public static void toXML(Document document, java.io.OutputStream outStream,
			String encoding) {
		//
		OutputFormat outformat = OutputFormat.createPrettyPrint();
		if (encoding == null || encoding.trim().equals("")) {
			encoding = DEFAULT_ENCODING;
		}
		// 设置编码类型
		outformat.setEncoding(encoding);
		outformat.setTrimText(false);
		XMLWriter xmlWriter = null;
		try {
			xmlWriter = new XMLWriter(outStream, outformat);
			xmlWriter.write(document);
			xmlWriter.flush();
		} catch (java.io.IOException ex) {
			throw new RuntimeException(ex);
		} finally {
			if (xmlWriter != null) {
				try {
					xmlWriter.close();
				} catch (java.io.IOException ex) {
				}
			}
		}
	}

	/**
	 * 把XML文档转化为String返回
	 * 
	 * @param document
	 *            要转化的XML的Document
	 * @param encoding
	 *            编码类型
	 * @return <code>String</code>
	 * @throws RuntimeException
	 *             如果有任何异常转化为该异常输出
	 */
	public static String toXML(Document document, String encoding) {
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		toXML(document, stream, encoding);
		if (stream != null) {
			try {
				stream.close();
			} catch (java.io.IOException ex) {
			}
		}
		return stream.toString();
	}

	// 自测试代码
	public static void main(String[] args) {
	}
}
