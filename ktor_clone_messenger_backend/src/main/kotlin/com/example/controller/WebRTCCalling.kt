package com.example.controller

import com.example.data.model.object_tranfer_socket.Member
import io.ktor.websocket.*
import kotlinx.coroutines.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

interface WebRTCCalling {
    fun handleState(session: Member)

    fun handleOffer(session: Member, message: String)

    fun handleAnswer(session: Member, message: String)

    fun handleICE(session: Member, message: String)

    fun onSessionStarted(session: Member)



    class Builder() {
        private lateinit var members: List<Member>
        private var state = ChatController.WebRTCCallingSessionState.Impossible
        private var scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

        private var handleState: (Member) -> Unit = {}
        private var handleOffer: (Member, String) -> Unit = { _, _ -> }
        private var handleAnswer: (Member, String) -> Unit = { _, _ -> }
        private var handleICE: (Member, String) -> Unit = { _, _ -> }
        fun setMembers(members: List<Member>): Builder {
            this.members = members
            return this
        }

        fun setHandleState(handle: (Member) -> Unit): Builder {
            handleState = handle
            return this
        }

        fun setHandleOffer(handle: (Member, String) -> Unit): Builder {
            handleOffer = handle
            return this
        }

        fun setHandleAnswer(handle: (Member, String) -> Unit): Builder {
            handleAnswer = handle
            return this
        }

        fun setHandleICE(handle: (Member, String) -> Unit): Builder {
            handleICE = handle
            return this
        }


        fun build(): WebRTCCalling {
            return object : WebRTCCalling {
                val member = members
                var callingState = state
                val callingScope = scope
                private val mutex = Mutex()
                override fun handleState(session: Member) {
                    callingScope.launch {
//                        handleState(session)
                        member.find { session.sessionId == it.sessionId }?.let {
                            it.socket.send(Frame.Text("${ChatController.WebRTCCommand.STATE} $callingState"))
                        }
                    }

                }

                override fun handleOffer(session: Member, message: String) {
                    callingScope.launch {
//                        handleOffer(session)
                        if (callingState != ChatController.WebRTCCallingSessionState.Ready) {
                            error("Session should be in ready state to handle offer")
                        }
                        callingState = ChatController.WebRTCCallingSessionState.Creating
                        println("handling offer from ${session.userId}")
                        notifyAboutStateUpdate()
                        member.filter { it.sessionId != session.sessionId }
                            .forEach {
                                it.socket.send(Frame.Text(message))
                            }
                    }
                }

                override fun handleAnswer(session: Member, message: String) {
                    callingScope.launch {
                        if (callingState != ChatController.WebRTCCallingSessionState.Creating) {
                            error("Session should be in ready state to handle answer")
                        }
                        println("handling answer from ${session.userId}")
                        member.filter { it.sessionId != session.sessionId }
                            .forEach {
                                it.socket.send(Frame.Text(message))
                            }

                        callingState = ChatController.WebRTCCallingSessionState.Creating
                        notifyAboutStateUpdate()
//                        handleAnswer(session, message)
                    }
                }

                override fun handleICE(session: Member, message: String) {
                    callingScope.launch {
//                        handleICE(session,message)
                        println("handling ice from ${session.userId}")
                        member.filter { it.sessionId != session.sessionId }
                            .forEach {
                                it.socket.send(Frame.Text(message))
                            }
                    }
                }

                override fun onSessionStarted(session: Member) {
                    callingScope.launch {
                        mutex.withLock {
                            if (member.size > 1) {
                                callingScope.launch(NonCancellable) {
                                    session.socket.send(Frame.Close()) // only two peers are supported
                                }
                                return@launch
                            }
                            session.socket.send("Added as a client: ${session.sessionId}")
                            if (member.size > 1) {
                                callingState = ChatController.WebRTCCallingSessionState.Ready
                            }
                            notifyAboutStateUpdate()
                        }
                    }
                }

                private fun notifyAboutStateUpdate() {
                    members.forEach { member ->
                        callingScope.launch {
                            member.socket.send(Frame.Text("${ChatController.WebRTCCommand.STATE} $callingState"))
                        }
                    }
                }
            }
        }
    }
}