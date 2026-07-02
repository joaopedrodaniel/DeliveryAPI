package com.example.distAPI.config;

import com.example.distAPI.model.entity.Administrador;
import com.example.distAPI.model.entity.Cliente;
import com.example.distAPI.model.entity.Endereco;
import com.example.distAPI.model.entity.Item;
import com.example.distAPI.model.entity.OrderStatus;
import com.example.distAPI.model.entity.Pedido;
import com.example.distAPI.model.entity.PerfilUsuario;
import com.example.distAPI.model.entity.Produto;
import com.example.distAPI.model.entity.StatusProduto;
import com.example.distAPI.model.entity.Vendedor;
import com.example.distAPI.model.repository.AdministradorRepository;
import com.example.distAPI.model.repository.ClienteRepository;
import com.example.distAPI.model.repository.EnderecoRepository;
import com.example.distAPI.model.repository.PedidoRepository;
import com.example.distAPI.model.repository.ProdutoRepository;
import com.example.distAPI.model.repository.VendedorRepository;
import com.example.distAPI.service.PedidoService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@Configuration
public class DatabaseSeeder {

    @Bean
    CommandLineRunner popularBanco(
            AdministradorRepository administradorRepository,
            ClienteRepository clienteRepository,
            VendedorRepository vendedorRepository,
            ProdutoRepository produtoRepository,
            EnderecoRepository enderecoRepository,
            PedidoRepository pedidoRepository,
            PedidoService pedidoService,
            PasswordEncoder passwordEncoder) {

        return args -> {
            Administrador admin = criarAdministrador(administradorRepository, passwordEncoder);
            List<Cliente> clientes = criarClientes(clienteRepository, passwordEncoder);
            List<Vendedor> vendedores = criarVendedores(vendedorRepository, passwordEncoder);
            List<Produto> produtos = criarProdutos(produtoRepository);
            List<Endereco> enderecos = criarEnderecos(enderecoRepository, clientes);
            criarPedidos(pedidoRepository, pedidoService, clientes, vendedores, produtos, enderecos);

            System.out.println("Banco populado com dados iniciais.");
            System.out.println("Admin: " + admin.getEmail() + " / admin123");
            System.out.println("Cliente: cliente1@email.com / 123456");
            System.out.println("Vendedor: vendedor1@email.com / 123456");
        };
    }

    private Administrador criarAdministrador(AdministradorRepository repository, PasswordEncoder passwordEncoder) {
        return repository.findByEmail("master@admin.com").orElseGet(() -> {
            Administrador admin = new Administrador();
            admin.setNome("Master Admin");
            admin.setEmail("master@admin.com");
            admin.setSenha(passwordEncoder.encode("admin123"));
            admin.setTemAcesso(true);
            admin.setPerfil(PerfilUsuario.ADMIN);
            return repository.save(admin);
        });
    }

    private List<Cliente> criarClientes(ClienteRepository repository, PasswordEncoder passwordEncoder) {
        Cliente cliente1 = repository.findByEmail("cliente1@email.com").orElseGet(() ->
                salvarCliente(repository, passwordEncoder, "Ana Cliente", "cliente1@email.com"));
        Cliente cliente2 = repository.findByEmail("cliente2@email.com").orElseGet(() ->
                salvarCliente(repository, passwordEncoder, "Bruno Cliente", "cliente2@email.com"));
        Cliente cliente3 = repository.findByEmail("cliente3@email.com").orElseGet(() ->
                salvarCliente(repository, passwordEncoder, "Carla Cliente", "cliente3@email.com"));

        return List.of(cliente1, cliente2, cliente3);
    }

    private Cliente salvarCliente(
            ClienteRepository repository,
            PasswordEncoder passwordEncoder,
            String nome,
            String email) {
        Cliente cliente = new Cliente();
        cliente.setNome(nome);
        cliente.setEmail(email);
        cliente.setSenha(passwordEncoder.encode("123456"));
        cliente.setPerfil(PerfilUsuario.CLIENTE);
        return repository.save(cliente);
    }

    private List<Vendedor> criarVendedores(VendedorRepository repository, PasswordEncoder passwordEncoder) {
        Vendedor vendedor1 = repository.findByEmail("vendedor1@email.com").orElseGet(() ->
                salvarVendedor(repository, passwordEncoder, "Julia Vendedora", "vendedor1@email.com", "VEN-001"));
        Vendedor vendedor2 = repository.findByEmail("vendedor2@email.com").orElseGet(() ->
                salvarVendedor(repository, passwordEncoder, "Marcos Vendedor", "vendedor2@email.com", "VEN-002"));

        return List.of(vendedor1, vendedor2);
    }

    private Vendedor salvarVendedor(
            VendedorRepository repository,
            PasswordEncoder passwordEncoder,
            String nome,
            String email,
            String matricula) {
        Vendedor vendedor = new Vendedor();
        vendedor.setNome(nome);
        vendedor.setEmail(email);
        vendedor.setSenha(passwordEncoder.encode("123456"));
        vendedor.setPerfil(PerfilUsuario.VENDEDOR);
        vendedor.setMatricula(matricula);
        return repository.save(vendedor);
    }

    private List<Produto> criarProdutos(ProdutoRepository repository) {
        return List.of(
                buscarOuCriarProduto(repository, "Agua Mineral 500ml", 3.50, 80, "Agua mineral sem gas", "https://placehold.co/400x400?text=Agua", "Agua"),
                buscarOuCriarProduto(repository, "Agua com Gas 500ml", 4.50, 60, "Agua mineral com gas", "https://placehold.co/400x400?text=Agua+Gas", "Agua"),
                buscarOuCriarProduto(repository, "Refrigerante Cola 2L", 10.90, 45, "Refrigerante sabor cola", "https://placehold.co/400x400?text=Cola", "Refrigerante"),
                buscarOuCriarProduto(repository, "Refrigerante Guarana 2L", 9.90, 40, "Refrigerante sabor guarana", "https://placehold.co/400x400?text=Guarana", "Refrigerante"),
                buscarOuCriarProduto(repository, "Suco de Uva 1L", 12.50, 30, "Suco integral de uva", "https://placehold.co/400x400?text=Suco", "Suco"),
                buscarOuCriarProduto(repository, "Cerveja Pilsen 350ml", 4.99, 120, "Cerveja lata pilsen", "https://placehold.co/400x400?text=Cerveja", "Cerveja"),
                buscarOuCriarProduto(repository, "Cerveja IPA 600ml", 15.90, 25, "Cerveja artesanal IPA", "https://placehold.co/400x400?text=IPA", "Cerveja"),
                buscarOuCriarProduto(repository, "Vinho Tinto 750ml", 39.90, 18, "Vinho tinto seco", "https://placehold.co/400x400?text=Vinho", "Vinho")
        );
    }

    private Produto buscarOuCriarProduto(
            ProdutoRepository repository,
            String nome,
            Double preco,
            Integer estoque,
            String descricao,
            String foto,
            String categoria) {
        return repository.findByNomeIgnoreCase(nome).orElseGet(() ->
                repository.save(produto(nome, preco, estoque, descricao, foto, categoria)));
    }

    private Produto produto(
            String nome,
            Double preco,
            Integer estoque,
            String descricao,
            String foto,
            String categoria) {
        Produto produto = new Produto();
        produto.setNome(nome);
        produto.setPreco(preco);
        produto.setEstoque(estoque);
        produto.setDescricao(descricao);
        produto.setFoto(foto);
        produto.setCategoria(categoria);
        produto.setStatus(StatusProduto.ATIVO);
        return produto;
    }

    private List<Endereco> criarEnderecos(EnderecoRepository repository, List<Cliente> clientes) {
        return List.of(
                buscarOuCriarEndereco(repository, clientes.get(0), "01001000", "Praca da Se", "100", "Apto 12", "Se", "Sao Paulo", "SP"),
                buscarOuCriarEndereco(repository, clientes.get(1), "30140071", "Avenida Afonso Pena", "500", "Sala 4", "Centro", "Belo Horizonte", "MG"),
                buscarOuCriarEndereco(repository, clientes.get(2), "20040002", "Rua da Assembleia", "80", "Bloco B", "Centro", "Rio de Janeiro", "RJ")
        );
    }

    private Endereco buscarOuCriarEndereco(
            EnderecoRepository repository,
            Cliente cliente,
            String cep,
            String logradouro,
            String numero,
            String complemento,
            String bairro,
            String cidade,
            String uf) {
        List<Endereco> enderecos = repository.findByClienteId(cliente.getId());
        if (!enderecos.isEmpty()) {
            return enderecos.get(0);
        }

        return repository.save(endereco(cliente, cep, logradouro, numero, complemento, bairro, cidade, uf));
    }

    private Endereco endereco(
            Cliente cliente,
            String cep,
            String logradouro,
            String numero,
            String complemento,
            String bairro,
            String cidade,
            String uf) {
        Endereco endereco = new Endereco();
        endereco.setCliente(cliente);
        endereco.setCep(cep);
        endereco.setLogradouro(logradouro);
        endereco.setNumero(numero);
        endereco.setComplemento(complemento);
        endereco.setBairro(bairro);
        endereco.setCidade(cidade);
        endereco.setUf(uf);
        return endereco;
    }

    private void criarPedidos(
            PedidoRepository pedidoRepository,
            PedidoService pedidoService,
            List<Cliente> clientes,
            List<Vendedor> vendedores,
            List<Produto> produtos,
            List<Endereco> enderecos) {
        if (pedidoRepository.count() > 0 || produtos.size() < 4 || enderecos.size() < 3) {
            return;
        }

        Pedido pedido1 = salvarPedido(pedidoService, clientes.get(0), vendedores.get(0), enderecos.get(0),
                List.of(item(produtos.get(0), 4), item(produtos.get(2), 1)));
        pedido1.setStatus(OrderStatus.ENTREGUE);
        pedidoRepository.save(pedido1);

        Pedido pedido2 = salvarPedido(pedidoService, clientes.get(1), vendedores.get(1), enderecos.get(1),
                List.of(item(produtos.get(5), 6), item(produtos.get(6), 2)));
        pedido2.setStatus(OrderStatus.ENVIADO);
        pedidoRepository.save(pedido2);

        salvarPedido(pedidoService, clientes.get(2), vendedores.get(0), enderecos.get(2),
                List.of(item(produtos.get(1), 3), item(produtos.get(4), 2)));
    }

    private Pedido salvarPedido(
            PedidoService pedidoService,
            Cliente cliente,
            Vendedor vendedor,
            Endereco endereco,
            List<Item> itens) {
        Pedido pedido = new Pedido();
        pedido.setCliente(cliente);
        pedido.setVendedor(vendedor);
        pedido.setEndereco(endereco);
        pedido.setItens(itens);
        return pedidoService.salvar(pedido);
    }

    private Item item(Produto produto, Integer quantidade) {
        Item item = new Item();
        item.setProduto(produto);
        item.setQuantidade(quantidade);
        item.setPrecoUnitario(produto.getPreco());
        return item;
    }
}
