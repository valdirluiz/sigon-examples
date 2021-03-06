package wumpus;

import agent.AgentLexer;
import agent.AgentParser;
import br.ufsc.ine.parser.AgentWalker;
import br.ufsc.ine.parser.VerboseListener;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import java.io.File;
import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        File agentFile = new File("wumpus.on");
        CharStream stream = CharStreams.fromFileName(agentFile.getAbsolutePath());
        AgentLexer lexer = new AgentLexer(stream);
        CommonTokenStream tokens = new CommonTokenStream(lexer);

        AgentParser parser = new AgentParser(tokens);
        parser.removeErrorListeners();
        parser.addErrorListener(new VerboseListener());

        ParseTree tree = parser.agent();
        ParseTreeWalker walker = new ParseTreeWalker();

        AgentWalker agentWalker = new AgentWalker();
        walker.walk(agentWalker, tree);
    }
}
