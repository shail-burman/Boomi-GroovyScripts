import groovy.io.FileType
import groovy.xml.*
import groovy.util.slurpersupport.*

def FOLDER_PATH = '../Data/'



def parseXMLFile(GPathResult Root, NodeRepo){
    Root.children().each {parseNode(it,NodeRepo)}
}

def parseNode(NodeChild Node, NodeRepo){
    if(Node.childNodes().size() > 1){
          Node.children().each{parseNode(it)}
    }
    else{
        if(Node.text() != '') {
            NodeKey = getNodeHierarchy(Node)
            NodeData = XmlUtil.serialize(Node).replaceAll('\\<\\?(.)*\\?\\>', '')
            if(NodeRepo.size() >0 ){
                if (!NodeRepo.ContainsKey(NodeKey)) {
                     NodeRepo.put(NodeKey, NodeData)
                }
                else{
                    NodeRepo.put(NodeKey, NodeData)
                }
            }
        }
    }
}

def getNodeHierarchy(NodeChild Node){
    if(Node.parent() != Node){
        return getNodeHierarchy(Node.parent()) + '\\' + Node.name()
    }
    else
        return Node.name()
}

def list = []


def dir = new File(FOLDER_PATH)
dir.eachFileRecurse (FileType.FILES) { file ->
    list << file
    def XmlData = new XmlSlurper().parse(file)
    println('*****  FILE: ' + file.name)
    parseXMLFile(XmlData, NodeRepo)
}
println(NodeRepo)
