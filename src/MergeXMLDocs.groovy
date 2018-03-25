import groovy.io.FileType
import groovy.util.slurpersupport.GPathResult
import groovy.util.slurpersupport.NodeChild

class MergeXMLDocs {

    private NodeRepo
    private xml_file_data = []

    MergeXMLDocs() {
        NodeRepo = [:]
    }

    public void addFileData(GPathResult xml_data) {
        xml_file_data << xml_data
    }

    void Process() {
        xml_file_data.each { xmlfile ->
            parsefFileData(xmlfile)
        }
        println NodeRepo
    }

    private void parsefFileData(GPathResult Root) {
        Root.children().each { parseNode(it) }
    }

    private void parseNode(NodeChild Node) {
        if (Node.childNodes().size() > 1) {
            Node.children().each { parseNode(it) }
        } else {
            if (Node.text() != '') {
                def NodeKey = getNodeHierarchy(Node)
                def NodeData = Node.text()//XmlUtil.serialize(Node).replaceAll('\\<\\?(.)*\\?\\>', '')
                if (this.NodeRepo.size() > 0) {
                    if (!this.NodeRepo.containsKey(NodeKey)) {
                        this.NodeRepo.put(NodeKey, NodeData)
                    }
                } else {
                    this.NodeRepo.put(NodeKey, NodeData)
                }
            }
        }
    }

    private String getNodeHierarchy(NodeChild Node) {
        if (Node.parent() != Node) {
            return getNodeHierarchy(Node.parent()) + '/' + Node.name()
        } else
            return Node.name()
    }


    static void main(String[] args) {
        MergeXMLDocs Merger = new MergeXMLDocs()
        def FOLDER_PATH = '../Data/'
        def dir = new File(FOLDER_PATH)
        dir.eachFileRecurse(FileType.FILES) { file ->
            Merger.addFileData(new XmlSlurper().parse(file))
        }
        Merger.Process()

    }

}
