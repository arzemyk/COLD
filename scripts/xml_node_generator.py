from xml.dom import minidom

class XMLNodeGenerator(object):
    def __init__(self, class_id):
        self.document = minidom.Document()
        self.class_id = class_id

    def create_skeleton_node(self, id):
        child = self.document.createElement('bean')
        child.setAttribute('id', id + '.skeleton')
        child.setAttribute('class', 'pl.edu.agh.toik.cold.proxy.ProxySkeleton')
        child.setAttribute('lazy-init', 'true')

        child.appendChild(self.create_constructor_ref_arg('cold.proxyActorSystem'))
        child.appendChild(self.create_constructor_ref_arg(id))
        child.appendChild(self.create_constructor_arg(id))

        return child


    def create_factory_node(self, id, url):
        child = self.document.createElement('bean')
        child.setAttribute('id', id)
        child.setAttribute('class', 'pl.edu.agh.toik.cold.proxy.ProxyStubFactory')
        child.setAttribute('factory-method', 'createProxyStub')
        child.setAttribute('lazy-init', 'true')

        child.appendChild(self.create_constructor_arg(self.class_id[id]))
        child.appendChild(self.create_constructor_arg(id))
        child.appendChild(self.create_constructor_ref_arg('cold.proxyActorSystem'))

        (host, port) = url.split(":")
        child.appendChild(self.create_constructor_arg(host))
        child.appendChild(self.create_constructor_arg(port))

        return child

    def create_constructor_ref_arg(self, value):
        carg = self.document.createElement('constructor-arg')
        ref = self.document.createElement('ref')
        ref.setAttribute('bean', value)
        carg.appendChild(ref)
        return carg

    def create_constructor_arg(self, value):
        carg = self.document.createElement('constructor-arg')
        val = self.document.createElement('value')
        text = self.document.createTextNode(value)
        val.appendChild(text)
        carg.appendChild(val)
        return carg

    def create_proxy_actor_system(self, url):
        child = self.document.createElement('bean')
        child.setAttribute('id', 'cold.proxyActorSystem')
        child.setAttribute('class', 'pl.edu.agh.toik.cold.proxy.ProxyActorSystem')

        (host, port) = url.split(":")
        child.appendChild(self.create_constructor_arg(host))
        child.appendChild(self.create_constructor_arg(port))

        return child

    def create_application_context(self):
        child = self.document.createElement('bean')
        child.setAttribute('id', 'cold.springApplicationContext')
        child.setAttribute('class', 'pl.edu.agh.toik.cold.utils.SpringApplicationContext')

        return child