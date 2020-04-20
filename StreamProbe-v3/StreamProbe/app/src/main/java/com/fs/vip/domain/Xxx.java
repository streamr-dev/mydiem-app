package com.fs.vip.domain;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Bytes32;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;
import rx.Observable;
import rx.functions.Func1;

/**
 * <p>Auto generated code.
 * <p><strong>Do not modify!</strong>
 * <p>Please use the <a href="https://docs.web3j.io/command_line.html">web3j command line tools</a>,
 * or the org.web3j.codegen.SolidityFunctionWrapperGenerator in the 
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 *
 * <p>Generated with web3j version 3.4.0.
 */
public class Xxx extends Contract {
    private static final String BINARY = "0x60806040526004361061015f576000357c0100000000000000000000000000000000000000000000000000000000900463ffffffff168063270231cc146101645780632e1a7d4d146101d357806332ef2b24146102005780634942e4cf1461029a5780634b319713146102e95780634beb62b7146103145780634e71e0c8146103a4578063543fd313146103bb578063570ca735146104125780636c7a9d24146104695780636dd4a7c9146105035780636ef610921461059357806371b15013146105ea57806373d8903b1461061557806385df51fd146106565780638beb60b61461069f5780638da5cb5b146106cc578063a0be06f914610723578063b3ab15fb1461074e578063b74d784e14610791578063c3ac610d146107bc578063c3b35a7e1461080f578063cb9b51c81461087c578063db518db21461092e578063e30c39781461097b578063f2fde38b146109d2578063fc0c546a14610a15575b600080fd5b34801561017057600080fd5b506101d1600480360381019080803573ffffffffffffffffffffffffffffffffffffffff1690602001909291908035906020019092919080359060200190929190803590602001908201803590602001919091929391929390505050610a6c565b005b3480156101df57600080fd5b506101fe60048036038101908080359060200190929190505050610b10565b005b34801561020c57600080fd5b5061029860048036038101908080359060200190929190803573ffffffffffffffffffffffffffffffffffffffff1690602001909291908035906020019092919080359060200190820180359060200190808060200260200160405190810160405280939291908181526020018383602002808284378201915050505050509192919290505050610b1e565b005b3480156102a657600080fd5b506102e76004803603810190808035906020019092919080359060200190929190803590602001908201803590602001919091929391929390505050610baf565b005b3480156102f557600080fd5b506102fe610bf0565b6040518082815260200191505060405180910390f35b34801561032057600080fd5b50610329610bf6565b6040518080602001828103825283818151815260200191508051906020019080838360005b8381101561036957808201518184015260208101905061034e565b50505050905090810190601f1680156103965780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b3480156103b057600080fd5b506103b9610c94565b005b3480156103c757600080fd5b506103fc600480360381019080803573ffffffffffffffffffffffffffffffffffffffff169060200190929190505050610e9e565b6040518082815260200191505060405180910390f35b34801561041e57600080fd5b50610427610eb6565b604051808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200191505060405180910390f35b34801561047557600080fd5b50610501600480360381019080803573ffffffffffffffffffffffffffffffffffffffff169060200190929190803590602001909291908035906020019092919080359060200190820180359060200190808060200260200160405190810160405280939291908181526020018383602002808284378201915050505050509192919290505050610edc565b005b34801561050f57600080fd5b50610575600480360381019080803560001916906020019092919080359060200190820180359060200190808060200260200160405190810160405280939291908181526020018383602002808284378201915050505050509192919290505050610f50565b60405180826000191660001916815260200191505060405180910390f35b34801561059f57600080fd5b506105d4600480360381019080803573ffffffffffffffffffffffffffffffffffffffff1690602001909291905050506110ff565b6040518082815260200191505060405180910390f35b3480156105f657600080fd5b506105ff611117565b6040518082815260200191505060405180910390f35b34801561062157600080fd5b506106406004803603810190808035906020019092919050505061111d565b6040518082815260200191505060405180910390f35b34801561066257600080fd5b5061068160048036038101908080359060200190929190505050611135565b60405180826000191660001916815260200191505060405180910390f35b3480156106ab57600080fd5b506106ca6004803603810190808035906020019092919050505061114d565b005b3480156106d857600080fd5b506106e16112fb565b604051808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200191505060405180910390f35b34801561072f57600080fd5b50610738611321565b6040518082815260200191505060405180910390f35b34801561075a57600080fd5b5061078f600480360381019080803573ffffffffffffffffffffffffffffffffffffffff169060200190929190505050611327565b005b34801561079d57600080fd5b506107a6611473565b6040518082815260200191505060405180910390f35b3480156107c857600080fd5b5061080d600480360381019080803590602001909291908035600019169060200190929190803590602001908201803590602001919091929391929390505050611479565b005b34801561081b57600080fd5b5061087a600480360381019080803573ffffffffffffffffffffffffffffffffffffffff169060200190929190803573ffffffffffffffffffffffffffffffffffffffff16906020019092919080359060200190929190505050611622565b005b34801561088857600080fd5b5061091460048036038101908080359060200190929190803573ffffffffffffffffffffffffffffffffffffffff169060200190929190803590602001909291908035906020019082018035906020019080806020026020016040519081016040528093929190818152602001838360200280828437820191505050505050919291929050505061197f565b604051808215151515815260200191505060405180910390f35b34801561093a57600080fd5b50610979600480360381019080803573ffffffffffffffffffffffffffffffffffffffff16906020019092919080359060200190929190505050611b00565b005b34801561098757600080fd5b50610990611b0f565b604051808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200191505060405180910390f35b3480156109de57600080fd5b50610a13600480360381019080803573ffffffffffffffffffffffffffffffffffffffff169060200190929190505050611b35565b005b348015610a2157600080fd5b50610a2a611c3e565b604051808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200191505060405180910390f35b6000610aa9853386868680806020026020016040519081016040528093929190818152602001838360200280828437820191505050505050610b1e565b610afb600960003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020016000205485611c6490919063ffffffff16565b9050610b08863383611622565b505050505050565b610b1b333383611622565b50565b610b2a8484848461197f565b1515610b9e576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040180806020018281038252600b8152602001807f6572726f725f70726f6f6600000000000000000000000000000000000000000081525060200191505060405180910390fd5b610ba9848484611c85565b50505050565b610bea338585858580806020026020016040519081016040528093929190818152602001838360200280828437820191505050505050610edc565b50505050565b600a5481565b600c8054600181600116156101000203166002900480601f016020809104026020016040519081016040528092919081815260200182805460018160011615610100020316600290048015610c8c5780601f10610c6157610100808354040283529160200191610c8c565b820191906000526020600020905b815481529060010190602001808311610c6f57829003601f168201915b505050505081565b600260009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff16141515610d59576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260108152602001807f6f6e6c7950656e64696e674f776e65720000000000000000000000000000000081525060200191505060405180910390fd5b600260009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16600160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff167f8be0079c531659141344cd1fd0a4f28419497f9722a3daafe3b4186f6b6457e060405160405180910390a3600260009054906101000a900473ffffffffffffffffffffffffffffffffffffffff16600160006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff1602179055506000600260006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff160217905550565b60086020528060005260406000206000915090505481565b600560009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1681565b6000610eea84868585610b1e565b610f3c600960008773ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020016000205484611c6490919063ffffffff16565b9050610f49858683611622565b5050505050565b6000806000849250600091505b83518260ff1610156110f757838260ff16815181101515610f7a57fe5b906020019060200201519050600060010281600019161415610f9b576110ea565b80600019168360001916101561104c5782816040516020018083600019166000191681526020018260001916600019168152602001925050506040516020818303038152906040526040518082805190602001908083835b6020831015156110185780518252602082019150602081019050602083039250610ff3565b6001836020036101000a038019825116818451168082178552505050505050905001915050604051809103902092506110e9565b80836040516020018083600019166000191681526020018260001916600019168152602001925050506040516020818303038152906040526040518082805190602001908083835b6020831015156110b95780518252602082019150602081019050602083039250611094565b6001836020036101000a038019825116818451168082178552505050505050905001915050604051809103902092505b5b8180600101925050610f5d565b505092915050565b60096020528060005260406000206000915090505481565b60035481565b60046020528060005260406000206000915090505481565b60006020528060005260406000206000915090505481565b600160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff16141515611212576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260098152602001807f6f6e6c794f776e6572000000000000000000000000000000000000000000000081525060200191505060405180910390fd5b670de0b6b3a7640000600654111515156112ba576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260228152602001807f41646d696e206665652063616e6e6f742062652067726561746572207468616e81526020017f203100000000000000000000000000000000000000000000000000000000000081525060400191505060405180910390fd5b806006819055507f11a80b766155f9b8f16a7da44d66269fd694cb1c247f4814244586f68dd53487816040518082815260200191505060405180910390a150565b600160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1681565b60065481565b600160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff161415156113ec576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260098152602001807f6f6e6c794f776e6572000000000000000000000000000000000000000000000081525060200191505060405180910390fd5b80600560006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff1602179055508073ffffffffffffffffffffffffffffffffffffffff167f4721129e0e676ed6a92909bb24e853ccdd63ad72280cc2e974e38e480e0e6e5460405160405180910390a250565b600b5481565b60606000600102600080878152602001908152602001600020546000191614151561150c576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040180806020018281038252600f8152602001807f6572726f725f6f7665727772697465000000000000000000000000000000000081525060200191505060405180910390fd5b82828080601f016020809104026020016040519081016040528093929190818152602001838380828437820191505050505050905061154c858583612009565b8360008087815260200190815260200160002081600019169055507f4268b0369742123cb8066ef715ccf53f3f1f710b5894e43e1ba1d159ed64e4c185858360405180848152602001836000191660001916815260200180602001828103825283818151815260200191508051906020019080838360005b838110156115df5780820151818401526020810190506115c4565b50505050905090810190601f16801561160c5780820380516001836020036101000a031916815260200191505b5094505050505060405180910390a15050505050565b6000808211151561169b576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260128152602001807f6572726f725f7a65726f5769746864726177000000000000000000000000000081525060200191505060405180910390fd5b6116ed82600960008673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020546120eb90919063ffffffff16565b9050600860008473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020016000205481111515156117a6576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040180806020018281038252600f8152602001807f6572726f725f6f7665726472616674000000000000000000000000000000000081525060200191505060405180910390fd5b80600960008573ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020819055506117ff82600a546120eb90919063ffffffff16565b600a81905550600760009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1663a9059cbb85846040518363ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401808373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200182815260200192505050602060405180830381600087803b1580156118ca57600080fd5b505af11580156118de573d6000803e3d6000fd5b505050506040513d60208110156118f457600080fd5b81019080805190602001909291905050501515611979576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040180806020018281038252600e8152602001807f6572726f725f7472616e7366657200000000000000000000000000000000000081525060200191505060405180910390fd5b50505050565b60008060008585604051602001808373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff166c01000000000000000000000000028152601401828152602001925050506040516020818303038152906040526040518082805190602001908083835b602083101515611a1c57805182526020820191506020810190506020830392506119f7565b6001836020036101000a038019825116818451168082178552505050505050905001915050604051809103902091506000808881526020019081526020016000205490506000600102816000191614151515611ae0576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260138152602001807f6572726f725f626c6f636b4e6f74466f756e640000000000000000000000000081525060200191505060405180910390fd5b611aea8285610f50565b6000191681600019161492505050949350505050565b611b0b828383611622565b5050565b600260009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1681565b600160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff16141515611bfa576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260098152602001807f6f6e6c794f776e6572000000000000000000000000000000000000000000000081525060200191505060405180910390fd5b80600260006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff16021790555050565b600760009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1681565b600080838311151515611c7657600080fd5b82840390508091505092915050565b600060046000858152602001908152602001600020549050600354810142111515611d18576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040180806020018281038252600c8152602001807f6572726f725f66726f7a656e000000000000000000000000000000000000000081525060200191505060405180910390fd5b81600860008573ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002054101515611dce576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260118152602001807f6572726f725f6f6c644561726e696e677300000000000000000000000000000081525060200191505060405180910390fd5b611e34600860008573ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002054611e2684600b546120eb90919063ffffffff16565b611c6490919063ffffffff16565b600b81905550600760009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff166370a08231306040518263ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001915050602060405180830381600087803b158015611ef757600080fd5b505af1158015611f0b573d6000803e3d6000fd5b505050506040513d6020811015611f2157600080fd5b8101908080519060200190929190505050611f49600a54600b54611c6490919063ffffffff16565b11151515611fbf576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260148152602001807f6572726f725f6d697373696e6742616c616e636500000000000000000000000081525060200191505060405180910390fd5b81600860008573ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020016000208190555050505050565b600560009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff161415156120ce576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260128152602001807f6572726f725f6e6f745065726d6974746564000000000000000000000000000081525060200191505060405180910390fd5b426004600085815260200190815260200160002081905550505050565b600080828401905083811015151561210257600080fd5b80915050929150505600a165627a7a72305820f639b0bb3911390662ef6be207c48280821a3429d59a459fb7a3ee03262554600029";

    public static final String FUNC_WITHDRAWALLTO = "withdrawAllTo";

    public static final String FUNC_WITHDRAW = "withdraw";

    public static final String FUNC_PROVE = "prove";

    public static final String FUNC_WITHDRAWALL = "withdrawAll";

    public static final String FUNC_TOTALWITHDRAWN = "totalWithdrawn";

    public static final String FUNC_JOINPARTSTREAM = "joinPartStream";

    public static final String FUNC_CLAIMOWNERSHIP = "claimOwnership";

    public static final String FUNC_EARNINGS = "earnings";

    public static final String FUNC_OPERATOR = "operator";

    public static final String FUNC_WITHDRAWALLFOR = "withdrawAllFor";

    public static final String FUNC_CALCULATEROOTHASH = "calculateRootHash";

    public static final String FUNC_WITHDRAWN = "withdrawn";

    public static final String FUNC_BLOCKFREEZESECONDS = "blockFreezeSeconds";

    public static final String FUNC_BLOCKTIMESTAMP = "blockTimestamp";

    public static final String FUNC_BLOCKHASH = "blockHash";

    public static final String FUNC_SETADMINFEE = "setAdminFee";

    public static final String FUNC_OWNER = "owner";

    public static final String FUNC_ADMINFEE = "adminFee";

    public static final String FUNC_SETOPERATOR = "setOperator";

    public static final String FUNC_TOTALPROVEN = "totalProven";

    public static final String FUNC_COMMIT = "commit";

    public static final String FUNC_WITHDRAWTO = "withdrawTo";

    public static final String FUNC_PROOFISCORRECT = "proofIsCorrect";

    public static final String FUNC_WITHDRAWFOR = "withdrawFor";

    public static final String FUNC_PENDINGOWNER = "pendingOwner";

    public static final String FUNC_TRANSFEROWNERSHIP = "transferOwnership";

    public static final String FUNC_TOKEN = "token";

    public static final Event OPERATORCHANGED_EVENT = new Event("OperatorChanged", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}),
            Arrays.<TypeReference<?>>asList());
    ;

    public static final Event ADMINFEECHANGED_EVENT = new Event("AdminFeeChanged", 
            Arrays.<TypeReference<?>>asList(),
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
    ;

    public static final Event OWNERSHIPTRANSFERRED_EVENT = new Event("OwnershipTransferred", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Address>() {}),
            Arrays.<TypeReference<?>>asList());
    ;

    public static final Event BLOCKCREATED_EVENT = new Event("BlockCreated", 
            Arrays.<TypeReference<?>>asList(),
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Bytes32>() {}, new TypeReference<Utf8String>() {}));
    ;

    protected Xxx(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected Xxx(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public RemoteCall<TransactionReceipt> withdrawAllTo(String recipient, BigInteger blockNumber, BigInteger totalEarnings, List<byte[]> proof) {
        final Function function = new Function(
                FUNC_WITHDRAWALLTO, 
                Arrays.<Type>asList(new Address(recipient),
                new Uint256(blockNumber),
                new Uint256(totalEarnings),
                new org.web3j.abi.datatypes.DynamicArray<Bytes32>(
                        org.web3j.abi.Utils.typeMap(proof, Bytes32.class))),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> withdraw(BigInteger amount) {
        final Function function = new Function(
                FUNC_WITHDRAW, 
                Arrays.<Type>asList(new Uint256(amount)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> prove(BigInteger blockNumber, String account, BigInteger balance, List<byte[]> proof) {
        final Function function = new Function(
                FUNC_PROVE, 
                Arrays.<Type>asList(new Uint256(blockNumber),
                new Address(account),
                new Uint256(balance),
                new org.web3j.abi.datatypes.DynamicArray<Bytes32>(
                        org.web3j.abi.Utils.typeMap(proof, Bytes32.class))),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> withdrawAll(BigInteger blockNumber, BigInteger totalEarnings, List<byte[]> proof) {
        final Function function = new Function(
                FUNC_WITHDRAWALL, 
                Arrays.<Type>asList(new Uint256(blockNumber),
                new Uint256(totalEarnings),
                new org.web3j.abi.datatypes.DynamicArray<Bytes32>(
                        org.web3j.abi.Utils.typeMap(proof, Bytes32.class))),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<BigInteger> totalWithdrawn() {
        final Function function = new Function(FUNC_TOTALWITHDRAWN, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<String> joinPartStream() {
        final Function function = new Function(FUNC_JOINPARTSTREAM, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<TransactionReceipt> claimOwnership() {
        final Function function = new Function(
                FUNC_CLAIMOWNERSHIP, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<BigInteger> earnings(String param0) {
        final Function function = new Function(FUNC_EARNINGS, 
                Arrays.<Type>asList(new Address(param0)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<String> operator() {
        final Function function = new Function(FUNC_OPERATOR, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<TransactionReceipt> withdrawAllFor(String recipient, BigInteger blockNumber, BigInteger totalEarnings, List<byte[]> proof) {
        final Function function = new Function(
                FUNC_WITHDRAWALLFOR, 
                Arrays.<Type>asList(new Address(recipient),
                new Uint256(blockNumber),
                new Uint256(totalEarnings),
                new org.web3j.abi.datatypes.DynamicArray<Bytes32>(
                        org.web3j.abi.Utils.typeMap(proof, Bytes32.class))),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<byte[]> calculateRootHash(byte[] hash, List<byte[]> others) {
        final Function function = new Function(FUNC_CALCULATEROOTHASH, 
                Arrays.<Type>asList(new Bytes32(hash),
                new org.web3j.abi.datatypes.DynamicArray<Bytes32>(
                        org.web3j.abi.Utils.typeMap(others, Bytes32.class))),
                Arrays.<TypeReference<?>>asList(new TypeReference<Bytes32>() {}));
        return executeRemoteCallSingleValueReturn(function, byte[].class);
    }

    public RemoteCall<BigInteger> withdrawn(String param0) {
        final Function function = new Function(FUNC_WITHDRAWN, 
                Arrays.<Type>asList(new Address(param0)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<BigInteger> blockFreezeSeconds() {
        final Function function = new Function(FUNC_BLOCKFREEZESECONDS, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<BigInteger> blockTimestamp(BigInteger param0) {
        final Function function = new Function(FUNC_BLOCKTIMESTAMP, 
                Arrays.<Type>asList(new Uint256(param0)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<byte[]> blockHash(BigInteger param0) {
        final Function function = new Function(FUNC_BLOCKHASH, 
                Arrays.<Type>asList(new Uint256(param0)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Bytes32>() {}));
        return executeRemoteCallSingleValueReturn(function, byte[].class);
    }

    public RemoteCall<TransactionReceipt> setAdminFee(BigInteger _adminFee) {
        final Function function = new Function(
                FUNC_SETADMINFEE, 
                Arrays.<Type>asList(new Uint256(_adminFee)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<String> owner() {
        final Function function = new Function(FUNC_OWNER, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<BigInteger> adminFee() {
        final Function function = new Function(FUNC_ADMINFEE, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<TransactionReceipt> setOperator(String newOperator) {
        final Function function = new Function(
                FUNC_SETOPERATOR, 
                Arrays.<Type>asList(new Address(newOperator)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<BigInteger> totalProven() {
        final Function function = new Function(FUNC_TOTALPROVEN, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<TransactionReceipt> commit(BigInteger blockNumber, byte[] rootHash, String ipfsHash) {
        final Function function = new Function(
                FUNC_COMMIT, 
                Arrays.<Type>asList(new Uint256(blockNumber),
                new Bytes32(rootHash),
                new Utf8String(ipfsHash)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> withdrawTo(String recipient, String account, BigInteger amount) {
        final Function function = new Function(
                FUNC_WITHDRAWTO, 
                Arrays.<Type>asList(new Address(recipient),
                new Address(account),
                new Uint256(amount)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<Boolean> proofIsCorrect(BigInteger blockNumber, String account, BigInteger balance, List<byte[]> proof) {
        final Function function = new Function(FUNC_PROOFISCORRECT, 
                Arrays.<Type>asList(new Uint256(blockNumber),
                new Address(account),
                new Uint256(balance),
                new org.web3j.abi.datatypes.DynamicArray<Bytes32>(
                        org.web3j.abi.Utils.typeMap(proof, Bytes32.class))),
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteCall<TransactionReceipt> withdrawFor(String recipient, BigInteger amount) {
        final Function function = new Function(
                FUNC_WITHDRAWFOR, 
                Arrays.<Type>asList(new Address(recipient),
                new Uint256(amount)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<String> pendingOwner() {
        final Function function = new Function(FUNC_PENDINGOWNER, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<TransactionReceipt> transferOwnership(String newOwner) {
        final Function function = new Function(
                FUNC_TRANSFEROWNERSHIP, 
                Arrays.<Type>asList(new Address(newOwner)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<String> token() {
        final Function function = new Function(FUNC_TOKEN, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public static RemoteCall<Xxx> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit, String operator, String joinPartStreamId, String tokenAddress, BigInteger blockFreezePeriodSeconds, BigInteger adminFeeFraction) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new Address(operator),
                new Utf8String(joinPartStreamId),
                new Address(tokenAddress),
                new Uint256(blockFreezePeriodSeconds),
                new Uint256(adminFeeFraction)));
        return deployRemoteCall(Xxx.class, web3j, credentials, gasPrice, gasLimit, BINARY, encodedConstructor);
    }

    public static RemoteCall<Xxx> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit, String operator, String joinPartStreamId, String tokenAddress, BigInteger blockFreezePeriodSeconds, BigInteger adminFeeFraction) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new Address(operator),
                new Utf8String(joinPartStreamId),
                new Address(tokenAddress),
                new Uint256(blockFreezePeriodSeconds),
                new Uint256(adminFeeFraction)));
        return deployRemoteCall(Xxx.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, encodedConstructor);
    }

    public List<OperatorChangedEventResponse> getOperatorChangedEvents(TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = extractEventParametersWithLog(OPERATORCHANGED_EVENT, transactionReceipt);
        ArrayList<OperatorChangedEventResponse> responses = new ArrayList<OperatorChangedEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            OperatorChangedEventResponse typedResponse = new OperatorChangedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.newOperator = (String) eventValues.getIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<OperatorChangedEventResponse> operatorChangedEventObservable(EthFilter filter) {
        return web3j.ethLogObservable(filter).map(new Func1<Log, OperatorChangedEventResponse>() {
            @Override
            public OperatorChangedEventResponse call(Log log) {
                EventValuesWithLog eventValues = extractEventParametersWithLog(OPERATORCHANGED_EVENT, log);
                OperatorChangedEventResponse typedResponse = new OperatorChangedEventResponse();
                typedResponse.log = log;
                typedResponse.newOperator = (String) eventValues.getIndexedValues().get(0).getValue();
                return typedResponse;
            }
        });
    }

    public Observable<OperatorChangedEventResponse> operatorChangedEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(OPERATORCHANGED_EVENT));
        return operatorChangedEventObservable(filter);
    }

    public List<AdminFeeChangedEventResponse> getAdminFeeChangedEvents(TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = extractEventParametersWithLog(ADMINFEECHANGED_EVENT, transactionReceipt);
        ArrayList<AdminFeeChangedEventResponse> responses = new ArrayList<AdminFeeChangedEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            AdminFeeChangedEventResponse typedResponse = new AdminFeeChangedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.adminFee = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<AdminFeeChangedEventResponse> adminFeeChangedEventObservable(EthFilter filter) {
        return web3j.ethLogObservable(filter).map(new Func1<Log, AdminFeeChangedEventResponse>() {
            @Override
            public AdminFeeChangedEventResponse call(Log log) {
                EventValuesWithLog eventValues = extractEventParametersWithLog(ADMINFEECHANGED_EVENT, log);
                AdminFeeChangedEventResponse typedResponse = new AdminFeeChangedEventResponse();
                typedResponse.log = log;
                typedResponse.adminFee = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
                return typedResponse;
            }
        });
    }

    public Observable<AdminFeeChangedEventResponse> adminFeeChangedEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(ADMINFEECHANGED_EVENT));
        return adminFeeChangedEventObservable(filter);
    }

    public List<OwnershipTransferredEventResponse> getOwnershipTransferredEvents(TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = extractEventParametersWithLog(OWNERSHIPTRANSFERRED_EVENT, transactionReceipt);
        ArrayList<OwnershipTransferredEventResponse> responses = new ArrayList<OwnershipTransferredEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            OwnershipTransferredEventResponse typedResponse = new OwnershipTransferredEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.previousOwner = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.newOwner = (String) eventValues.getIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<OwnershipTransferredEventResponse> ownershipTransferredEventObservable(EthFilter filter) {
        return web3j.ethLogObservable(filter).map(new Func1<Log, OwnershipTransferredEventResponse>() {
            @Override
            public OwnershipTransferredEventResponse call(Log log) {
                EventValuesWithLog eventValues = extractEventParametersWithLog(OWNERSHIPTRANSFERRED_EVENT, log);
                OwnershipTransferredEventResponse typedResponse = new OwnershipTransferredEventResponse();
                typedResponse.log = log;
                typedResponse.previousOwner = (String) eventValues.getIndexedValues().get(0).getValue();
                typedResponse.newOwner = (String) eventValues.getIndexedValues().get(1).getValue();
                return typedResponse;
            }
        });
    }

    public Observable<OwnershipTransferredEventResponse> ownershipTransferredEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(OWNERSHIPTRANSFERRED_EVENT));
        return ownershipTransferredEventObservable(filter);
    }

    public List<BlockCreatedEventResponse> getBlockCreatedEvents(TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = extractEventParametersWithLog(BLOCKCREATED_EVENT, transactionReceipt);
        ArrayList<BlockCreatedEventResponse> responses = new ArrayList<BlockCreatedEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            BlockCreatedEventResponse typedResponse = new BlockCreatedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.blockNumber = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.rootHash = (byte[]) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse.ipfsHash = (String) eventValues.getNonIndexedValues().get(2).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<BlockCreatedEventResponse> blockCreatedEventObservable(EthFilter filter) {
        return web3j.ethLogObservable(filter).map(new Func1<Log, BlockCreatedEventResponse>() {
            @Override
            public BlockCreatedEventResponse call(Log log) {
                EventValuesWithLog eventValues = extractEventParametersWithLog(BLOCKCREATED_EVENT, log);
                BlockCreatedEventResponse typedResponse = new BlockCreatedEventResponse();
                typedResponse.log = log;
                typedResponse.blockNumber = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse.rootHash = (byte[]) eventValues.getNonIndexedValues().get(1).getValue();
                typedResponse.ipfsHash = (String) eventValues.getNonIndexedValues().get(2).getValue();
                return typedResponse;
            }
        });
    }

    public Observable<BlockCreatedEventResponse> blockCreatedEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(BLOCKCREATED_EVENT));
        return blockCreatedEventObservable(filter);
    }

    public static Xxx load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new Xxx(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    public static Xxx load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new Xxx(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static class OperatorChangedEventResponse {
        public Log log;

        public String newOperator;
    }

    public static class AdminFeeChangedEventResponse {
        public Log log;

        public BigInteger adminFee;
    }

    public static class OwnershipTransferredEventResponse {
        public Log log;

        public String previousOwner;

        public String newOwner;
    }

    public static class BlockCreatedEventResponse {
        public Log log;

        public BigInteger blockNumber;

        public byte[] rootHash;

        public String ipfsHash;
    }
}
