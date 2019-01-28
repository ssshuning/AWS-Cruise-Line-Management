
package cmu.edu.andrew.shuningc;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the cmu.edu.andrew.shuningc package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _IOException_QNAME = new QName("http://shuningc.andrew.edu.cmu/", "IOException");
    private final static QName _NoSuchAlgorithmException_QNAME = new QName("http://shuningc.andrew.edu.cmu/", "NoSuchAlgorithmException");
    private final static QName _ParserConfigurationException_QNAME = new QName("http://shuningc.andrew.edu.cmu/", "ParserConfigurationException");
    private final static QName _DealMessage_QNAME = new QName("http://shuningc.andrew.edu.cmu/", "dealMessage");
    private final static QName _DealMessageResponse_QNAME = new QName("http://shuningc.andrew.edu.cmu/", "dealMessageResponse");
    private final static QName _SAXException_QNAME = new QName("http://shuningc.andrew.edu.cmu/", "SAXException");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: cmu.edu.andrew.shuningc
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link IOException }
     * 
     */
    public IOException createIOException() {
        return new IOException();
    }

    /**
     * Create an instance of {@link NoSuchAlgorithmException }
     * 
     */
    public NoSuchAlgorithmException createNoSuchAlgorithmException() {
        return new NoSuchAlgorithmException();
    }

    /**
     * Create an instance of {@link ParserConfigurationException }
     * 
     */
    public ParserConfigurationException createParserConfigurationException() {
        return new ParserConfigurationException();
    }

    /**
     * Create an instance of {@link DealMessage }
     * 
     */
    public DealMessage createDealMessage() {
        return new DealMessage();
    }

    /**
     * Create an instance of {@link DealMessageResponse }
     * 
     */
    public DealMessageResponse createDealMessageResponse() {
        return new DealMessageResponse();
    }

    /**
     * Create an instance of {@link SAXException }
     * 
     */
    public SAXException createSAXException() {
        return new SAXException();
    }

    /**
     * Create an instance of {@link Exception }
     * 
     */
    public Exception createException() {
        return new Exception();
    }

    /**
     * Create an instance of {@link Throwable }
     * 
     */
    public Throwable createThrowable() {
        return new Throwable();
    }

    /**
     * Create an instance of {@link StackTraceElement }
     * 
     */
    public StackTraceElement createStackTraceElement() {
        return new StackTraceElement();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link IOException }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://shuningc.andrew.edu.cmu/", name = "IOException")
    public JAXBElement<IOException> createIOException(IOException value) {
        return new JAXBElement<IOException>(_IOException_QNAME, IOException.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link NoSuchAlgorithmException }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://shuningc.andrew.edu.cmu/", name = "NoSuchAlgorithmException")
    public JAXBElement<NoSuchAlgorithmException> createNoSuchAlgorithmException(NoSuchAlgorithmException value) {
        return new JAXBElement<NoSuchAlgorithmException>(_NoSuchAlgorithmException_QNAME, NoSuchAlgorithmException.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ParserConfigurationException }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://shuningc.andrew.edu.cmu/", name = "ParserConfigurationException")
    public JAXBElement<ParserConfigurationException> createParserConfigurationException(ParserConfigurationException value) {
        return new JAXBElement<ParserConfigurationException>(_ParserConfigurationException_QNAME, ParserConfigurationException.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DealMessage }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://shuningc.andrew.edu.cmu/", name = "dealMessage")
    public JAXBElement<DealMessage> createDealMessage(DealMessage value) {
        return new JAXBElement<DealMessage>(_DealMessage_QNAME, DealMessage.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DealMessageResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://shuningc.andrew.edu.cmu/", name = "dealMessageResponse")
    public JAXBElement<DealMessageResponse> createDealMessageResponse(DealMessageResponse value) {
        return new JAXBElement<DealMessageResponse>(_DealMessageResponse_QNAME, DealMessageResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SAXException }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://shuningc.andrew.edu.cmu/", name = "SAXException")
    public JAXBElement<SAXException> createSAXException(SAXException value) {
        return new JAXBElement<SAXException>(_SAXException_QNAME, SAXException.class, null, value);
    }

}
