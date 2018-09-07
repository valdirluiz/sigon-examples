package avaliacao.contextos;

import alice.tuprolog.InvalidTheoryException;
import alice.tuprolog.MalformedGoalException;
import alice.tuprolog.NoMoreSolutionException;
import alice.tuprolog.NoSolutionException;
import alice.tuprolog.Prolog;
import alice.tuprolog.SolveInfo;
import alice.tuprolog.Term;
import alice.tuprolog.Theory;
import alice.tuprolog.UnknownVarException;
import br.ufsc.ine.agent.bridgerules.Body;
import br.ufsc.ine.agent.bridgerules.BridgeRule;
import br.ufsc.ine.agent.bridgerules.Head;
import br.ufsc.ine.agent.context.custom.CustomContext;

public class AvaliacaoContextos {

	public static void main(String[] args) {

		CustomContext _x = new CustomContext("X");
		CustomContext _y = new CustomContext("Y");
		CustomContext _z = new CustomContext("Z");

		_y.appendFact("a.");
		_y.appendFact("b.");
		_y.appendFact("c.");

		_z.appendFact("b.");
		_z.appendFact("c.");
		_z.appendFact("d.");

		Head xHead = Head.builder().context(_x).clause("T").build();
		Body yBody = Body.builder().context(_y).clause("T").build();
		Body zBody = Body.builder().context(_z).clause("T").build();

		BridgeRule bridgeRule = BridgeRule.builder().head(xHead).body(yBody.and(zBody)).build();

		bridgeRule.execute();

		System.out.println("Estado do contexto Y antes execução da regra: ");
		System.out.println(_y.getTheory().toString());

		System.out.println("Estado do contexto Z antes execução da regra: ");
		System.out.println(_z.getTheory().toString());

		System.out.println("Estado do contexto X após execução da regra de ponte: ");
		System.out.println(_x.getTheory().toString());
	}

	 
}
