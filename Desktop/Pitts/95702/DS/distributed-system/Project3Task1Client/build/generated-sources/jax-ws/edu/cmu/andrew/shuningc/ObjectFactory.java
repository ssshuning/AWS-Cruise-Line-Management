
package edu.cmu.andrew.shuningc;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the edu.cmu.andrew.shuningc package. 
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

    private final static QName _AddBlock_QNAME = new QName("http://shuningc.andrew.cmu.edu/", "addBlock");
    private final static QName _View_QNAME = new QName("http://shuningc.andrew.cmu.edu/", "view");
    private final static QName _AddBlockResponse_QNAME = new QName("http://shuningc.andrew.cmu.edu/", "addBlockResponse");
    private final static QName _ViewResponse_QNAME = new QName("http://shuningc.andrew.cmu.edu/", "viewResponse");
    private final static QName _NoSuchAlgorithmException_QNAME = new QName("http://shuningc.andrew.cmu.edu/", "NoSuchAlgorithmException");
    private final static QName _Verify_QNAME = new QName("http://shuningc.andrew.cmu.edu/", "verify");
    private final static QName _VerifyResponse_QNAME = new QName("http://shuningc.andrew.cmu.edu/", "verifyResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: edu.cmu.andrew.shuningc
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link View }
     * 
     */
    public View createView() {
        return new View();
    }

    /**
     * Create an instance of {@link AddBlock }
     * 
     */
    public AddBlock createAddBlock() {
        return new AddBlock();
    }

    /**
     * Create an instance of {@link VerifyResponse }
     * 
     */
    public VerifyResponse createVerifyResponse() {
        return new VerifyResponse();
    }

    /**
     * Create an instance of {@link Verify }
     * 
     */
    public Verify createVerify() {
        return new Verify();
    }

    /**
     * Create an instance of {@link NoSuchAlgorithmException }
     * 
     */
    public NoSuchAlgorithmException createNoSuchAlgorithmException() {
        return new NoSuchAlgorithmException();
    }

    /**
     * Create an instance of {@link ViewResponse }
     * 
     */
    public ViewResponse createViewResponse() {
        return new ViewResponse();
    }

    /**
     * Create an instance of {@link AddBlockResponse }
     * 
     */
    public AddBlockResponse createAddBlockResponse() {
        return new AddBlockResponse();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AddBlock }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://shuningc.andrew.cmu.edu/", name = "addBlock")
    public JAXBElement<AddBlock> createAddBlock(AddBlock value) {
        return new JAXBElement<AddBlock>(_AddBlock_QNAME, AddBlock.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link View }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://shuningc.andrew.cmu.edu/", name = "view")
    public JAXBElement<View> createView(View value) {
        return new JAXBElement<View>(_View_QNAME, View.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AddBlockResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://shuningc.andrew.cmu.edu/", name = "addBlockResponse")
    public JAXBElement<AddBlockResponse> createAddBlockResponse(AddBlockResponse value) {
        return new JAXBElement<AddBlockResponse>(_AddBlockResponse_QNAME, AddBlockResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ViewResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://shuningc.andrew.cmu.edu/", name = "viewResponse")
    public JAXBElement<ViewResponse> createViewResponse(ViewResponse value) {
        return new JAXBElement<ViewResponse>(_ViewResponse_QNAME, ViewResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link NoSuchAlgorithmException }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://shuningc.andrew.cmu.edu/", name = "NoSuchAlgorithmException")
    public JAXBElement<NoSuchAlgorithmException> createNoSuchAlgorithmException(NoSuchAlgorithmException value) {
        return new JAXBElement<NoSuchAlgorithmException>(_NoSuchAlgorithmException_QNAME, NoSuchAlgorithmException.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Verify }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://shuningc.andrew.cmu.edu/", name = "verify")
    public JAXBElement<Verify> createVerify(Verify value) {
        return new JAXBElement<Verify>(_Verify_QNAME, Verify.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link VerifyResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://shuningc.andrew.cmu.edu/", name = "verifyResponse")
    public JAXBElement<VerifyResponse> createVerifyResponse(VerifyResponse value) {
        return new JAXBElement<VerifyResponse>(_VerifyResponse_QNAME, VerifyResponse.class, null, value);
    }

}
